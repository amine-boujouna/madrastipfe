package com.sip.store.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
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
import com.sip.store.entities.Contact;
import com.sip.store.entities.News;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ArticleRepository;
import com.sip.store.repositories.ContactRepository;
import com.sip.store.repositories.NewsRepository;
import com.sip.store.repositories.ProviderRepository;
@Controller
@RequestMapping("/contact")
public class ContactController {
	private final ContactRepository contactRepository;
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @Autowired
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    
   
    
    @GetMapping("/")
    public String showArticleFormToUpdate(Model model) {
    	long id=1;
    	Contact contact = contactRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	
        model.addAttribute("contact", contact);
        
        return "contact/contact";
    }
    @PostMapping("/update")
    public String updateNews(@Valid Contact contact, BindingResult result,  Model model) {
    	long id=1;
        if (result.hasErrors()) {
        	contact.setId(id);
            return "contact/contact";
        }
       System.out.print("ok");
        contactRepository.save(contact);
        return "redirect:../dashboard";
    }

    

}
