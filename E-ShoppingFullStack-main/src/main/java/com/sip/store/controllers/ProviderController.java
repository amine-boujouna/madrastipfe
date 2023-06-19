package com.sip.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sip.store.entities.Article;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ProviderRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import javax.validation.Valid;
@Controller
@RequestMapping("/providers/")
public class ProviderController {
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	private final ProviderRepository providerRepository;
	
    @Autowired
    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }


    
    @GetMapping("list")
    //@ResponseBody
    public String listProviders(Model model) {
    	
    	
        model.addAttribute("providers", providerRepository.findAll());
        
        return "providers/listProviders.html";
         
    }
    
    @GetMapping("add")
    public String showAddProviderForm(Model model) {
    	Provider provider = new Provider();// object dont la valeur des attributs par defaut
    	model.addAttribute("provider", provider);
        return "providers/addProvider";
    }
    
    @PostMapping("add")
    public String addProvider(@Valid Provider provider, BindingResult result,
            @RequestParam("files") MultipartFile[] files) {
        if (result.hasErrors()) {
            return "providers/addProvider";
        }
     // upload du ficher
       StringBuilder fileName = new StringBuilder();
    	MultipartFile file = files[0];
    	Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
    	
    	fileName.append(file.getOriginalFilename());
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
        provider.setLogo(fileName.toString());

        System.out.println(provider);
        providerRepository.save(provider);
        return "redirect:list";
    }


    
    @GetMapping("delete/{id}")
    public String deleteProvider(@PathVariable("id") long id, Model model) {
    	
    	
    	//long id2 = 100L;
    	
        Provider provider = providerRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
        
        System.out.println("suite du programme...");
        
        providerRepository.delete(provider);
        
        /*model.addAttribute("providers", providerRepository.findAll());
        return "providers/listProviders";*/
        return "redirect:../list";
    }
    
    
    @GetMapping("edit/{id}")
    public String showProviderFormToUpdate(@PathVariable("id") long id, Model model) {
        Provider provider = providerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
        
        model.addAttribute("provider", provider);
        
        return "providers/updateProvider";
    }




    
    @PostMapping("update/{id}")
    public String updateProvider(@PathVariable("id") long id, @Valid Provider provider, BindingResult result, Model model, @RequestParam("files") MultipartFile[] files) {
    	 if (result.hasErrors()) {
         	provider.setId(id);
             return "providers/updateProvider";
         }
    	/// part upload
     	StringBuilder fileName = new StringBuilder();
     	MultipartFile file = files[0];
     	if(!file.isEmpty()) {
 	    	Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
 	    	
 	    	fileName.append(file.getOriginalFilename());
 			  try {
 				Files.write(fileNameAndPath, file.getBytes());
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 			provider.setLogo(fileName.toString());
     	} 
    	providerRepository.save(provider);
    	return"redirect:../list";
    	
    }
    
    
    @GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") long id, Model model) {
		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		List<Article> articles = providerRepository.findArticlesByProvider(id);
		for (Article a : articles)
System.out.println("Article = " + a.getLabel());
		
		model.addAttribute("articles", articles);
		model.addAttribute("provider", provider);
		return "providers/showProvider";
	}


        		
}
