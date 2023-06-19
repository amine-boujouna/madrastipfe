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
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ArticleRepository;
import com.sip.store.repositories.ProviderRepository;
@Controller
@RequestMapping("/article/")
public class ArticleController {
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @Autowired
    public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
        this.articleRepository = articleRepository;
        this.providerRepository = providerRepository;
    }
    
    @GetMapping("list")
    public String listProviders(Model model) {
    	//model.addAttribute("articles", null);
        model.addAttribute("articles", articleRepository.findAll());
        return "article/listArticles";
    }
    
    @GetMapping("add")
    public String showAddArticleForm(Article article, Model model) {
    	
    	model.addAttribute("providers", providerRepository.findAll());
    	model.addAttribute("article", new Article());
    	return "article/addArticle";
    }
    
    @PostMapping("add")
    //@ResponseBody
    public String addArticle(@Valid Article article, BindingResult result, @RequestParam(name = "providerId", required = false) Long p,@RequestParam("files") MultipartFile[] files,@RequestParam("filesProfilePicture") MultipartFile[] filesProfilePicture) {
    	
    	Provider provider = providerRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + p));
    	article.setProvider(provider);
    	
    	/// part upload
    	StringBuilder fileName = new StringBuilder();
    	MultipartFile file = files[0];
    	Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
    	
    	fileName.append(file.getOriginalFilename());
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
		  StringBuilder fileName1 = new StringBuilder();
	    	MultipartFile file1 = filesProfilePicture[0];
	    	Path fileNameAndPath1 = Paths.get(uploadDirectory, file1.getOriginalFilename());
	    	
	    	fileName1.append(file1.getOriginalFilename());
			  try {
				Files.write(fileNameAndPath1, file1.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			  article.setFacePicture(fileName.toString());
		 article.setProfilePicture(fileName1.toString());


    	
    	 articleRepository.save(article);
    	 return "redirect:list";
    	
    	//return article.getLabel() + " " +article.getPrice() + " " + p.toString();
    }
    
    @GetMapping("delete/{id}")
    public String deleteProvider(@PathVariable("id") long id, Model model) {
        Article artice = articleRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
        articleRepository.delete(artice);
        model.addAttribute("articles", articleRepository.findAll());
        return "article/listArticles";
    }
    
    @GetMapping("edit/{id}")
    public String showArticleFormToUpdate(@PathVariable("id") long id, Model model) {
    	Article article = articleRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	
        model.addAttribute("article", article);
        model.addAttribute("providers", providerRepository.findAll());
        model.addAttribute("idProvider", article.getProvider().getId());
        
        return "article/updateArticle";
    }
    @PostMapping("edit/{id}")
    public String updateArticle(@PathVariable("id") long id, @Valid Article article, BindingResult result,
        Model model, @RequestParam(name = "providerId", required = false) Long p, @RequestParam("files") MultipartFile[] files, @RequestParam("filesProfilePicture") MultipartFile[] filesProfilePicture) {
        if (result.hasErrors()) {
        	article.setId(id);
            return "article/updateArticle";
        }
        Provider provider = providerRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + p));
    	article.setProvider(provider);
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
			article.setFacePicture(fileName.toString());
    	} 
		StringBuilder fileName1 = new StringBuilder();
	    MultipartFile file1 = filesProfilePicture[0];
		    if(!file1.isEmpty()) {
		    	Path fileNameAndPath1 = Paths.get(uploadDirectory, file1.getOriginalFilename());
		    	
		    	fileName1.append(file1.getOriginalFilename());
				  try {
					Files.write(fileNameAndPath1, file1.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			 article.setProfilePicture(fileName1.toString());
	    }
        articleRepository.save(article);
        model.addAttribute("articles", articleRepository.findAll());
        return "article/listArticles";
    }

    @GetMapping("show/{id}")
    public String showArticleDetails(@PathVariable("id") long id, Model model) {
    	Article article = articleRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	
        model.addAttribute("article", article);
        
        return "article/showArticle";
    }

}
