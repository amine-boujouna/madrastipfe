package com.sip.store.controllers;

import com.sip.store.email.EmailSender;
import com.sip.store.entities.*;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.RoleRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;

@Controller
@RequestMapping("/users/")
public class UserController {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    private RoleRepository roleRepository;
    @Autowired
    private ClasseRepository classeRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired

    FileStorageService storageService;

    public static String uploadDirectory = "C:/work/madrastipfe/E-ShoppingFullStack-main/src/main/resources/static/uploads";

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }


    @GetMapping("list")
    //@ResponseBody
    public String listUser(Model model) {
        List<User> userList = (List<User>) userRepository.findAll();
        if (userList.isEmpty()) {
            userList = null;
        }
        model.addAttribute("user", userList);
        return "users/listUser";
    }

    @GetMapping("listProfesseur")
    //@ResponseBody
    public String listProfessuer(Model model) {
        List<User> userList = (List<User>) userRepository.findAll();
        if (userList.isEmpty()) {
            userList = null;
        }
        model.addAttribute("user", userList);
        return "Professeur/listProfesseur";
    }

    @GetMapping("listAdministrateur")
    //@ResponseBody
    public String listAdministrateur(Model model) {
        List<User> userList = (List<User>) userRepository.findAll();
        if (userList.isEmpty()) {
            userList = null;
        }
        model.addAttribute("user", userList);
        return "Administrateur/listAdministrateur";
    }

    @GetMapping("add")
    public String showAddUserForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("classes", classeRepository.findAll());
        model.addAttribute("user", user);
        return "users/addUser";
    }

    @GetMapping("addProfesseur")
    public String showAddProfForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("user", user);
        return "Professeur/addProfesseur";
    }

    @GetMapping("addAdministrateur")
    public String showAddAdminForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("user", user);
        return "Administrateur/addAdministrateur";
    }

    @PostMapping("add")
    public String addUser(@Valid User user, @RequestParam("files") MultipartFile[] files,@RequestParam(name = "classeId", required = false) Long d) {

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
        Classe classe = classeRepository.findById(d)
                .orElseThrow(()-> new IllegalArgumentException("Invalid classe Id:" + d));
        user.setClasseList(new HashSet<Classe>(Arrays.asList(classe)));
        user.setPhoto(fileName.toString());
        user.setActive(0);
        Role userRole = roleRepository.findByRole("Eleve");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);

        String username= user.getPassword();
        String link = user.getUsername();
        String nom=user.getEmail();
        String firstname=user.getNom();
        emailSender.send(
                nom,
                buildEmail(firstname, link,username));
        return "redirect:list";
    }

    @PostMapping("addProfesseur")
    public String addProfesseur(@Valid User user, @RequestParam("files") MultipartFile[] files) {

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
        Role userRole = roleRepository.findByRole("Professeur");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);
   /*    try {
            sendEmail(user.getEmail(), user.getUsername(), user.getPassword());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    */

        String username= user.getPassword();
        String link = user.getUsername();
        String nom=user.getEmail();
        String firstname=user.getNom();
        emailSender.send(
                nom,
                buildEmail(firstname, link,username));

        return "redirect:listProfesseur";
    }


    private String buildEmail(String name, String link,String username) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Compte ajoutée avec succès</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">salut " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> username: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + link + "</p></blockquote>\n  <p>password</p> "+
                "        \n" + "<p>"+username+" </p>"+
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    @PostMapping("addAdministrateur")
    public String addAdministrateur(@Valid User user, @RequestParam("files") MultipartFile[] files) {

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
        Role userRole = roleRepository.findByRole("SUPERADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);


        return "redirect:listAdministrateur";
    }


    @Transactional
    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        userRepository.deleteuser(id);
        return "redirect:../list";
    }

    @Transactional
    @GetMapping("deleteProfesseur/{id}")
    public String deleteProfesseur(@PathVariable("id") long id, Model model) {
        userRepository.deleteuser(id);
        return "redirect:../listProfesseur";
    }

    @Transactional
    @GetMapping("deleteAdministrateur/{id}")
    public String deleteAdministrateur(@PathVariable("id") long id, Model model) {
        userRepository.deleteuser(id);
        return "redirect:../listAdministrateur";
    }


    @GetMapping("edit/{id}")
    public String showUserFormToUpdate(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/updateUser";
    }

    @GetMapping("editProfesseur/{id}")
    public String showProfesseurFormToUpdate(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "Professeur/updateProfesseur";
    }

    @GetMapping("editAdministrateur/{id}")
    public String showAdministrateurFormToUpdate(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "Administrateur/updateAdministrateur";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "users/updateUser";
        }
        User profile = null;
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            profile = user1.get();
            profile.setEmail(user.getEmail());
            profile.setGenre(user.getGenre());
            profile.setNom(user.getNom());
            profile.setPrenom(user.getPrenom());
            profile.setAdresse(user.getAdresse());
            profile.setBiographie(user.getBiographie());
            profile.setSalaire(user.getSalaire());
            profile.setSpecialite(user.getSpecialite());
            profile.setTel(user.getTel());
            profile.setUsername(user.getUsername());
            profile.setPassword(user.getPassword());
            profile.setNommere(user.getNommere());
            profile.setNompere(user.getNompere());
            userRepository.save(profile);
        }
        return "redirect:../list";
    }

    @PostMapping("updateProfesseur/{id}")
    public String updateProfesseur(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model, @RequestParam("files") MultipartFile[] files) {
        if (result.hasErrors()) {
            user.setId(id);
            return "Professeur/updateProfesseur";
        }
        User profile = null;
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            profile = user1.get();
            profile.setEmail(user.getEmail());
            profile.setGenre(user.getGenre());
            profile.setNom(user.getNom());
            profile.setPrenom(user.getPrenom());
            profile.setAdresse(user.getAdresse());
            profile.setBiographie(user.getBiographie());
            profile.setSalaire(user.getSalaire());
            profile.setSpecialite(user.getSpecialite());
            profile.setTel(user.getTel());
            profile.setUsername(user.getUsername());
            profile.setPassword(user.getPassword());

            StringBuilder fileName = new StringBuilder();
            MultipartFile file = files[0];
            //System.out.println(file.isEmpty());
            if (!file.isEmpty()) {
                Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());

                fileName.append(file.getOriginalFilename());
                try {
                    Files.write(fileNameAndPath, file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.setPhoto(fileName.toString());
            }

            userRepository.save(profile);
        }
            return "redirect:../listProfesseur";
        }

    @PostMapping("updateAdministrateur/{id}")
    public String updateAdministrateur(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "Administrateur/updateAdministrateur";
        }
        User profile = null;
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            profile = user1.get();
            profile.setEmail(user.getEmail());
            profile.setGenre(user.getGenre());
            profile.setNom(user.getNom());
            profile.setPrenom(user.getPrenom());
            profile.setAdresse(user.getAdresse());
            profile.setBiographie(user.getBiographie());
            profile.setSalaire(user.getSalaire());
            profile.setSpecialite(user.getSpecialite());
            profile.setTel(user.getTel());
            profile.setUsername(user.getUsername());
            profile.setPassword(user.getPassword());
            profile.setNommere(user.getNommere());
            profile.setNompere(user.getNompere());
            userRepository.save(profile);
        }
        return "redirect:../listAdministrateur";
    }


    @GetMapping("show/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/showUser";
    }
    @GetMapping("showProfesseur/{id}")
    public String showProfesseur(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "Professeur/showProfesseur";
    }
    @GetMapping("showAdministrateur/{id}")
    public String showAdministrateur(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "Administrateur/showAdministrateur";
    }




}
