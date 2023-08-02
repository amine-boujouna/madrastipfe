package com.sip.store.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sip.store.entities.Reglement;
import com.sip.store.entities.ReglementEnseignant;
import com.sip.store.entities.User;
import com.sip.store.repositories.RegelementEnseignantRepository;
import com.sip.store.repositories.ReglementRepository;
import com.sip.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Date;

@Controller
@RequestMapping("/reglementEnseignant/")

public class ReglementEnseignantController {
    @Autowired
    private RegelementEnseignantRepository regelementEnseignantRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("list")
    public String listegelment(Model model) {
        model.addAttribute("reglement", regelementEnseignantRepository.findAll());
        return "reglementEnseignant/listreglementEnseignant";
    }
    @GetMapping("add")
    public String showAddReglementForm(Model model) {

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("reglement", new ReglementEnseignant());
        return "reglementEnseignant/ajouterreglementsenseignant";
    }

    @PostMapping("/add")
    public String enregistrerReglement(@ModelAttribute ReglementEnseignant reglementEnseignant, @RequestParam(name = "userid", required = false) Long d) {
        User user = userRepository.findById(d)
                .orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + d));
        reglementEnseignant.setUser(user);
        // Sauvegarder le règlement dans la base de données
        reglementEnseignant.setAnnee(String.valueOf(new Date()));
        regelementEnseignantRepository.save(reglementEnseignant);

        // Générer le PDF avec les informations du règlement
        byte[] pdfBytes = generatePdf(reglementEnseignant);

        // Envoyer l'e-mail avec le PDF en pièce jointe
        sendEmailWithAttachment(reglementEnseignant.getUser().getEmail(), pdfBytes);
        return "redirect:list";
    }

   /* public static byte[] generatePdf(ReglementEnseignant reglementEnseignant) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Ajouter les informations du règlement dans le PDF
            document.add(new Paragraph("ID: " + reglementEnseignant.getId()));
            document.add(new Paragraph("année: " + reglementEnseignant.getAnnee()));
            document.add(new Paragraph("datedepaimenet: " + reglementEnseignant.getDatedepaimenet()));
            document.add(new Paragraph("titre: " + reglementEnseignant.getTitre()));
            document.add(new Paragraph("moisdepais: " + reglementEnseignant.getMoisdepais()));
            document.add(new Paragraph("intitulebanque: " + reglementEnseignant.getIntitulebanque()));
            document.add(new Paragraph("modedepaimenet: " + reglementEnseignant.getModedepaimenet()));
            document.add(new Paragraph("avance: " + reglementEnseignant.getAvance()));
            document.add(new Paragraph("Categorie: " + reglementEnseignant.getCategorie()));
            document.add(new Paragraph("cin: " + reglementEnseignant.getCin()));
            document.add(new Paragraph("congeeAnnuel: " + reglementEnseignant.getCongeeAnnuel()));
            document.add(new Paragraph("contributionsociale: " + reglementEnseignant.getContributionsociale()));
            document.add(new Paragraph("deductions: " + reglementEnseignant.getDeductions()));
            document.add(new Paragraph("Echelon: " + reglementEnseignant.getEchelon()));
            document.add(new Paragraph("heuresuplemaentaire: " + reglementEnseignant.getHeuresuplemaentaire()));
            document.add(new Paragraph("netapayer: " + reglementEnseignant.getNetapayer()));
            document.add(new Paragraph("nombredheuredabsence: " + reglementEnseignant.getNombredheuredabsence()));
            document.add(new Paragraph("numcnss: " + reglementEnseignant.getNumcnss()));
            document.add(new Paragraph("numcompte: " + reglementEnseignant.getNumcompte()));
            document.add(new Paragraph("prime: " + reglementEnseignant.getPrime()));
            document.add(new Paragraph("salairebrut: " + reglementEnseignant.getSalairebrut()));
            document.add(new Paragraph("Tauxhoraire: " + reglementEnseignant.getTauxhoraire()));
            document.add(new Paragraph("soldeconge: " + reglementEnseignant.getSoldeconge()));
            document.add(new Paragraph("salairenet: " + reglementEnseignant.getSalairenet()));
            document.add(new Paragraph("salairedebase: " + reglementEnseignant.getSalairedebase()));
            document.add(new Paragraph("nom: " + reglementEnseignant.getUser().getNom()));
            document.add(new Paragraph("prenom: " + reglementEnseignant.getUser().getPrenom()));
            document.add(new Paragraph("Matricule: " + reglementEnseignant.getMatricule()));
            document.add(new Paragraph("retenueCNSS: " + reglementEnseignant.getRetenueCNSS()));
            document.add(new Paragraph("congeprisparmois: " + reglementEnseignant.getCongeprisparmois()));


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

            // Attachez le PDF au message
            helper.addAttachment("reglement.pdf", new ByteArrayResource(attachment));

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Gérez les exceptions ici (par exemple, si l'e-mail ne peut pas être envoyé)
            e.printStackTrace();
        }
    }
   /* public static byte[] generatePdf(ReglementEnseignant reglementEnseignant) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Ajouter les informations du règlement dans le PDF
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            // Première ligne du tableau
            addCell(table, "Matricule: " + reglementEnseignant.getMatricule());
            addCell(table, "CIN: " + reglementEnseignant.getCin());
            addCell(table, "Num CNSS: " + reglementEnseignant.getNumcnss());
            addCell(table, "Num Compte: " + reglementEnseignant.getNumcompte());

            // Deuxième ligne du tableau
            addCell(table, "Catégorie: " + reglementEnseignant.getCategorie());
            addCell(table, "Echelon: " + reglementEnseignant.getEchelon());
            addCell(table, "Salaire de base: " + reglementEnseignant.getSalairedebase());
            addCell(table, "Taux Horaire: " + reglementEnseignant.getTauxhoraire());

            // Troisième ligne du tableau
            addCell(table, "Année: " + reglementEnseignant.getAnnee());
            addCell(table, "Mois de paiement: " + reglementEnseignant.getMoisdepais());
            addCell(table, "Date de paiement: " + reglementEnseignant.getDatedepaimenet());
            addCell(table, " ");

            // Quatrième ligne du tableau
            addCell(table, "Nom: " + reglementEnseignant.getUser().getNom());
            addCell(table, "Prénom: " + reglementEnseignant.getUser().getPrenom());
            addCell(table, " ");
            addCell(table, " ");

            // Cinquième ligne du tableau
            addCell(table, "Salaire de base: " + reglementEnseignant.getSalairedebase());
            addCell(table, "Prime: " + reglementEnseignant.getPrime());
            addCell(table, "Heures Supplémentaires: " + reglementEnseignant.getHeuresuplemaentaire());
            addCell(table, "Congé Annuel: " + reglementEnseignant.getCongeeAnnuel());

            // Sixième ligne du tableau
            addCell(table, "Retenue CNSS: " + reglementEnseignant.getRetenueCNSS());
            addCell(table, "Salaire Brut: " + reglementEnseignant.getSalairebrut());
            addCell(table, "Déductions: " + reglementEnseignant.getDeductions());
            addCell(table, "Salaire Net: " + reglementEnseignant.getSalairenet());

            // Septième ligne du tableau
            addCell(table, "Avance: " + reglementEnseignant.getAvance());
            addCell(table, "Net à Payer: " + reglementEnseignant.getNetapayer());
            addCell(table, " ");
            addCell(table, " ");

            // Huitième ligne du tableau
            addCell(table, "Mode de Paiement: " + reglementEnseignant.getModedepaimenet());
            addCell(table, "Intitulé Banque: " + reglementEnseignant.getIntitulebanque());
            addCell(table, "Num Compte: " + reglementEnseignant.getNumcompte());
            addCell(table, " ");

            // Neuvième ligne du tableau
            addCell(table, "Congé Pris par Mois: " + reglementEnseignant.getCongeprisparmois());
            addCell(table, " ");
            addCell(table, " ");
            addCell(table, " ");

            document.add(table);

            document.close();

            return outputStream.toByteArray();
        } catch (DocumentException e) {
            // Gérer les exceptions liées à la génération du PDF ici
            e.printStackTrace();
            return null;
        }
    }

    private static void addCell(PdfPTable table, String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setPadding(5);
        table.addCell(cell);
    }

    */
   public static byte[] generatePdf(ReglementEnseignant reglementEnseignant) {
       try {
           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           Document document = new Document();
           PdfWriter.getInstance(document, outputStream);

           document.open();
           Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
           Paragraph title = new Paragraph("Fiche de paie enseignant ", titleFont);
           title.setAlignment(Element.ALIGN_CENTER);
           document.add(title);

           // Ajouter les informations du règlement dans le PDF
           PdfPTable table = new PdfPTable(4);
           table.setWidthPercentage(100);
           table.setSpacingBefore(20);
           table.setSpacingAfter(20);

           // Première ligne du tableau
           addCell(table, "Matricule: " + reglementEnseignant.getMatricule() +  "\nCIN: " + reglementEnseignant.getCin()+ "\nNum CNSS: " + reglementEnseignant.getNumcnss(),1);
           addCell(table, "Catégorie: " + reglementEnseignant.getCategorie() + "\nEchelon: " + reglementEnseignant.getEchelon()+ "\nSalaire de base: " + reglementEnseignant.getSalairedebase()+ "\nTaux Horaire: " + reglementEnseignant.getTauxhoraire(),1);
           addCell(table, "Année: " + reglementEnseignant.getAnnee()+"\nMois de paiement: " + reglementEnseignant.getMoisdepais()+ "\nDate de paiement: " + reglementEnseignant.getDatedepaimenet(),1);
           addCell(table, "Nom: " + reglementEnseignant.getUser().getNom() + "\nPrénom: " + reglementEnseignant.getUser().getPrenom(), 1);
           addCell(table,  "Salaire de base: " + reglementEnseignant.getSalairedebase()
                   + "\nPrime: " + reglementEnseignant.getPrime()+ "\nCongé Annuel: " + reglementEnseignant.getCongeeAnnuel()+ "\nRetenue CNSS: " + reglementEnseignant.getRetenueCNSS()
                   + "\nSalaire Brut: " + reglementEnseignant.getSalairebrut()+ "\nDéductions: " + reglementEnseignant.getDeductions()
                   + "\nSalaire Net: " + reglementEnseignant.getSalairenet()+ "\nAvance: " + reglementEnseignant.getAvance()
                   + "\nNet à Payer: " + reglementEnseignant.getNetapayer(),2);
           addCell(table, "Mode de Paiement: " + reglementEnseignant.getModedepaimenet()+ "\nIntitulé Banque: " + reglementEnseignant.getIntitulebanque()+ "\nNum Compte: " + reglementEnseignant.getNumcompte()+ "\nCongé Pris par Mois: " + reglementEnseignant.getCongeprisparmois() + "\nnombre d'heure d'absece" + reglementEnseignant.getNombredheuredabsence(), 2);
           document.add(table);

           document.close();

           return outputStream.toByteArray();
       } catch (DocumentException e) {
           // Gérer les exceptions liées à la génération du PDF ici
           e.printStackTrace();
           return null;
       }
   }

    private static void addCell(PdfPTable table, String content, int colspan) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setPadding(5);
        cell.setColspan(colspan);
        table.addCell(cell);
    }
}



