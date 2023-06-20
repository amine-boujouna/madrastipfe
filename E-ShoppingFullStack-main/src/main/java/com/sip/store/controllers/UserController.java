package com.sip.store.controllers;

import com.sip.store.entities.*;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.DepartementRepository;
import com.sip.store.repositories.RoleRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/")
public class UserController {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ClasseRepository classeRepository;

    private RoleRepository roleRepository;
    private JavaMailSender mailSender;
    @Autowired
    FileStorageService storageService;

    public static String uploadDirectory =  "C:/work/madrastipfe/E-ShoppingFullStack-main/src/main/resources/static/uploads";

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }



    @GetMapping("list")
    //@ResponseBody
    public String listUser(Model model) {


      /*  List<User> la = (List<User>)userRepository.findAll();
        if(la.size()==0)
            la=null;
        model.addAttribute("user", la);

       */
        List<User> userList = (List<User>) userRepository.findAll();
        if (userList.isEmpty()) {
            userList = null;
        }
        model.addAttribute("user", userList);

        // Ajouter le code de débogage pour afficher la valeur de user.getPhoto()
      //  for (User user : userList) {
        //    System.out.println("Photo: " + user.getPhoto());
        //}
        return "users/listUser";

    }

    @GetMapping("add")
    public String showAddUserForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("user", user);
        return "users/addUser";
    }

    @PostMapping("add")
    public String addUser(@Valid User user, @RequestParam("files") MultipartFile[] files)  {

        StringBuilder fileName = new StringBuilder();
        MultipartFile file = files[0];
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileName.append(file.getOriginalFilename());
        System.out.println("Path: " + fileNameAndPath.toAbsolutePath().toString());
        // Vérification et création du répertoire si nécessaire
        Path directoryPath = fileNameAndPath.getParent();
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer l'erreur de création du répertoire
            }
        }
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'écriture du fichier
        }
        user.setPhoto(fileName.toString());
        user.setActive(0);
        Role userRole = roleRepository.findByRole("Eleve");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);


        return "redirect:list";
    }





    @Transactional
    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
/*
        User user = userRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + id));

        userRepository.delete(user);*/
        userRepository.deleteuser(id);
        return "redirect:../list";
    }


    @GetMapping("edit/{id}")
    public String showUserFormToUpdate(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);

        return "users/updateUser";
    }





    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "users/updateUser";
        }
        userRepository.save(user);
        return "redirect:../list";

    }


    @GetMapping("show/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/showUser";
    }



}
