package com.sip.store.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sip.store.entities.Article;
import com.sip.store.entities.Messaging;
import com.sip.store.entities.Provider;
import com.sip.store.entities.User;
import com.sip.store.repositories.ArticleRepository;
import com.sip.store.repositories.MessagingRepository;
import com.sip.store.repositories.ProviderRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.UserService;
@Controller
@RequestMapping("/messaging/")
public class MessagingController {
	@Autowired
    private UserService userService;
	private final UserRepository userRepository;
	private final MessagingRepository messagingRepository;
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @Autowired
    public MessagingController(UserRepository userRepository, MessagingRepository messagingRepository) {
        this.userRepository = userRepository;
        this.messagingRepository = messagingRepository;
    }
    
    @GetMapping("list")
    public String listMessagings(Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userConnected = userService.findUserByEmail(auth.getName());
        System.out.println(userConnected.getId());
        model.addAttribute("messagings", messagingRepository.findAllMessageOfCurrentUser(userConnected));
        //model.addAttribute("messagings", messagingRepository.findAll());
        return "messaging/listMessaging";
    }
    
    @GetMapping("add")
    public String showAddMessagingForm(Messaging messaging, Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userConnected = userService.findUserByEmail(auth.getName());
    	model.addAttribute("users", userRepository.findAll());
    	model.addAttribute("messaging", new Messaging());
    	return "messaging/addMessaging";
    }
    
    @PostMapping("add")
    //@ResponseBody
    public String addMessaging(@Valid Messaging messaging, BindingResult result, @RequestParam(name = "userId", required = false) Integer p) {
    	
    	 User user = userRepository.findById(p).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + p));
    	messaging.setRecipient(user);
    	messaging.setStatus("Non lu");
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userConnected = userService.findUserByEmail(auth.getName());
    	messaging.setSender(userConnected);
    	messaging.setSentDate(java.time.LocalDate.now());
    	 messagingRepository.save(messaging);
    	 return "redirect:list";
    	
    }
    
    @GetMapping("delete/{id}")
    public String deleteMessaging(@PathVariable("id") long id, Model model) {
        Messaging messaging = messagingRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
        messagingRepository.delete(messaging);
        model.addAttribute("messagings", messagingRepository.findAll());
        return "messaging/listMessaging";
    }
    


    @GetMapping("show/{id}")
    public String showMessagingDetails(@PathVariable("id") long id, Model model) {
    	Messaging messaging = messagingRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	messaging.setStatus("Lu");
    	messagingRepository.save(messaging);
        model.addAttribute("messaging", messaging);
        
        return "messaging/showMessaging";
    }

}
