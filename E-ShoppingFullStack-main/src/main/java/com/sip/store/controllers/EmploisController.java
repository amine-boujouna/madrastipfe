package com.sip.store.controllers;

import com.sip.store.entities.Classe;
import com.sip.store.entities.Departement;
import com.sip.store.entities.Emploi;
import com.sip.store.entities.FileInfo;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.EmploiRepository;
import com.sip.store.services.FileStorageService;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.Resource;
import javax.naming.ldap.PagedResultsControl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/emplois/")

public class EmploisController {
           @Autowired
           private ClasseRepository classeRepository;
           @Autowired
           private EmploiRepository emploiRepository;

    private static final String UPLOAD_DIR = "C:/work/madrastipfe/E-ShoppingFullStack-main/src/uploads/"; // Directory to store uploaded files
    @GetMapping("list")
    public String listNiveau(Model model) {
        model.addAttribute("emploi", emploiRepository.findAll());
        return "emplois/listemplois";
    }

    @GetMapping("/add")
    public String showAddEmploiForm(Model model) {
        model.addAttribute("emploi", new Emploi());
        model.addAttribute("classes", classeRepository.findAll());
        return "emplois/addemplois";
    }
    @PostMapping("/add")
    public String saveEmploi(@ModelAttribute Emploi emploi, @RequestParam("pdfFile") MultipartFile pdfFile
            ,@RequestParam(name = "classeid", required = false) Long d) {
        if (!pdfFile.isEmpty()) {
            try {
                // Get the bytes of the PDF file
                byte[] pdfBytes = pdfFile.getBytes();

                // Save the PDF file in the upload directory with a unique name
                Path pdfFilePath = Paths.get(UPLOAD_DIR + pdfFile.getOriginalFilename());
                Files.write(pdfFilePath, pdfBytes);

                // Set the PDF file name in the emploi entity
                emploi.setPdf(pdfFile.getOriginalFilename());

            } catch (IOException e) {
                // Handle the exception (e.g., log or show an error message)
                e.printStackTrace();
            }
        }
        Classe classe= classeRepository.findById(d)
                .orElseThrow(()-> new IllegalArgumentException("Invalid classe Id:" + d));
        emploi.setClasse(classe);
        emploiRepository.save(emploi);
        return "redirect:list"; // Redirect to the add-emploi page after saving
    }
}
