package com.sip.store.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sip.store.entities.*;
import com.sip.store.repositories.ReglementRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.FileStorageService;
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
        public String enregistrerReglement(@ModelAttribute Reglement reglement,@RequestParam(name = "userid", required = false) Long d) {
            User user = userRepository.findById(d)
                    .orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + d));
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

    public static byte[] generatePdf(Reglement reglement) {
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



}
