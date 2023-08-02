package com.sip.store.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sip.store.entities.*;
import com.sip.store.repositories.ReglementRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.FileStorageService;
import com.sip.store.services.ReglementService;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/reglement/")
public class ReglementsController {
    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReglementService reglementService;

    @GetMapping("list")
    public String listegelment(Model model) {
        model.addAttribute("reglement", reglementRepository.findAll());
        return "reglements/listreglements";
    }

    @GetMapping("add")
    public String showAddReglementForm(Model model) {

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("reglement", new Reglement());
        return "reglements/ajouterreglements";
    }

    @PostMapping("/add")
    public String enregistrerReglement(@ModelAttribute Reglement reglement, @RequestParam(name = "userid", required = false) Long d) {
        User user = userRepository.findById(d)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + d));
        reglement.setUser(user);
        // Sauvegarder le règlement dans la base de données
        reglement.setDate(new Date());
        reglementRepository.save(reglement);

        // Générer le PDF avec les informations du règlement
        byte[] pdfBytes = generatePdf(reglement);

        // Envoyer l'e-mail avec le PDF en pièce jointe
        sendEmailWithAttachment(reglement.getUser().getEmail(), pdfBytes);
        sendEmailWithAttachment(reglement.getUser().getEmailpere(), pdfBytes);
        return "redirect:list";
    }

   /* public static byte[] generatePdf(Reglement reglement) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Ajouter les informations du règlement dans le PDF
            document.add(new Paragraph("ID: " + reglement.getId()));
            document.add(new Paragraph("Durée: " + reglement.getDuree()));
            document.add(new Paragraph("Montant: " + reglement.getMontant()));
            document.add(new Paragraph("Modalité: " + reglement.getModalite()));
            document.add(new Paragraph("Remarque: " + reglement.getRemarque()));
            document.add(new Paragraph("Date: " + reglement.getDate()));

            document.close();

            return outputStream.toByteArray();
        } catch (DocumentException e) {
            // Gérer les exceptions liées à la génération du PDF ici
            e.printStackTrace();
            return null;
        }
    }

    */

    private void sendEmailWithAttachment(String userEmail, byte[] attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Confirmation de règlement");
            helper.setText("Bonjour, veuillez trouver ci-joint le PDF contenant les informations de votre règlement.");

            helper.addAttachment("reglement.pdf", new ByteArrayResource(attachment));

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] generatePdf(Reglement reglement) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Logo de l'école (facultatif)
            // Exemple :
            // Image img = Image.getInstance("chemin_vers_logo.png");
            // document.add(img);

            // Titre du PDF
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("ÉCOLE PRIVÉE Madrasti", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Informations sur le paiement
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            addRow(table, "Nom de l'élève", reglement.getUser().getNom());
            addRow(table, "Nom de l'élève", reglement.getUser().getPrenom());
            addRow(table, "Durée", reglement.getDuree());
            addRow(table, "Montant payé", reglement.getMontant());
            addRow(table, "Modalité de paiement", reglement.getModalite());
            addRow(table, "Date du paiement", new SimpleDateFormat("dd/MM/yyyy").format(reglement.getDate()));
            addRow(table, "Remarque", reglement.getRemarque());

            document.add(table);

            // Message de remerciement
            Font thankYouFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Paragraph thankYou = new Paragraph("Merci pour votre paiement !", thankYouFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            return outputStream.toByteArray();
        } catch (DocumentException e) {
            // Gérer les exceptions liées à la génération du PDF ici
            e.printStackTrace();
            return null;
        }
    }

    private static void addRow(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Paragraph(label));
        PdfPCell cellValue = new PdfPCell(new Paragraph(value));
        table.addCell(cellLabel);
        table.addCell(cellValue);
    }

 /*   @GetMapping("/departements-montant")
    public String getDepartementsAndMontantForUsers(Model model) {
        List<Object[]> results = reglementService.getDepartementAndTotalMontantForUsers();
        model.addAttribute("departementsAndMontant", results);
        return "stat/affichestat";
    }*/
/* @GetMapping("/departements-montant")
 public List<Object[]> getDepartementsAndMontantForUsers() {
     return reglementService.getDepartementAndTotalMontantForUsers();
 }

 */
  /*  @GetMapping("/departements-montant")
    public String showResults(Model model) {
        List<Object[]> resultList = reglementRepository.countDepartementAndTotalMontantForUsers();
        model.addAttribute("results", resultList);
        return "stat/affichestat";
    }

   */


 @GetMapping("/departements-montant")
 public String showResults(Model model) {
     List<Object[]> resultList = reglementRepository.countDepartementAndTotalMontantForUsers();
     List<DepartementMontantDTO> dtos = convertToDTO(resultList);
     model.addAttribute("results", dtos);
     for (DepartementMontantDTO dto : dtos) {
         System.out.println("Departement: " + dto.getDepartement() + ", Username: " + dto.getUsername() + ", Total Montant: " + dto.getTotalMontant());
     }
     return "stat/affichestat";
 }

    // Méthode pour convertir la liste d'Object[] en une liste d'objets DepartementMontantDTO
    private List<DepartementMontantDTO> convertToDTO(List<Object[]> resultList) {
        List<DepartementMontantDTO> dtos = new ArrayList<>();
        for (Object[] result : resultList) {
            DepartementMontantDTO dto = new DepartementMontantDTO();
            dto.setDepartement((String) result[0]);
            dto.setUsername((String) result[1]);

            // Convertir le totalMontant de String en Double
            String totalMontantStr = (String) result[2];
            double totalMontant = Double.parseDouble(totalMontantStr);
            dto.setTotalMontant(totalMontant);

            dtos.add(dto);
        }
        return dtos;
    }
    @GetMapping("/departements-montant-json")
    @ResponseBody // Ajouter cette annotation pour renvoyer les données au format JSON
    public List<DepartementMontantDTO> getDepartementsMontantJSON() {
        List<Object[]> resultList = reglementRepository.countDepartementAndTotalMontantForUsers();
        List<DepartementMontantDTO> dtos = convertToDTO(resultList);

        // Ajouter des logs pour vérifier les données récupérées
        for (DepartementMontantDTO dto : dtos) {
            System.out.println("Departement: " + dto.getDepartement() + ", Username: " + dto.getUsername() + ", Total Montant: " + dto.getTotalMontant());
        }

        return dtos;
    }


}






