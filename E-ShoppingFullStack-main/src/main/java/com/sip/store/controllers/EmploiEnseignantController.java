package com.sip.store.controllers;

import com.sip.store.entities.Classe;
import com.sip.store.entities.Emploi;
import com.sip.store.entities.User;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.EmploiRepository;
import com.sip.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequestMapping("/emploii/")
public class EmploiEnseignantController {
    @Autowired
    private ClasseRepository classeRepository;
    @Autowired
    private EmploiRepository emploiRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "C:/work/madrastipfe/E-ShoppingFullStack-main/src/uploads/"; // Directory to store uploaded files

    @GetMapping("listemploienseignant")
    public String listemploienseignant(Model model) {
        model.addAttribute("emploii", emploiRepository.findAll());
        return "emploisenseignant/listemploienseignant";
    }
    @GetMapping("addemploienseignant")
    public String showAddEmploiFormenseignant(Model model) {
        model.addAttribute("emploi", new Emploi());
        model.addAttribute("classes", classeRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "emploisenseignant/addemploisenseignant";
    }
    @PostMapping("addemploienseignant")
    public String saveEmploienseignant(@ModelAttribute Emploi emploi, @RequestParam("pdfFile") MultipartFile pdfFile
            ,@RequestParam(name = "classeid", required = false) Long d,@RequestParam(name = "userid", required = false) Long u) {
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
        User user= userRepository.findById(u)
                .orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + d));
        emploi.setUser(user);
        emploiRepository.save(emploi);
        return "redirect:listemploienseignant"; // Redirect to the add-emploi page after saving
    }
}
