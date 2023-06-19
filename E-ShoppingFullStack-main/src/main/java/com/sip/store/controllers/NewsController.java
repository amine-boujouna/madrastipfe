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

import com.sip.store.entities.News;
import com.sip.store.repositories.NewsRepository;
@Controller
@RequestMapping("/news/")
public class NewsController {
	private final NewsRepository newsRepository;
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }
    
    @GetMapping("list")
    public String listNews(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        return "news/listNews";
    }
    
    
    @GetMapping("add")
    public String showAddNewsForm(News news, Model model) {
    	
    	model.addAttribute("news", new News());
    	return "news/addNews";
    }
    
    @PostMapping("add")
    //@ResponseBody
    public String addNews(@Valid News news, BindingResult result, @RequestParam("files") MultipartFile[] files) {
    	
    	
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
		  
	
			  news.setPhoto(fileName.toString());


    	
    	 newsRepository.save(news);
    	 return "redirect:list";
    	
    }
    
    @GetMapping("delete/{id}")
    public String deleteNews(@PathVariable("id") long id, Model model) {
        News news = newsRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
        newsRepository.delete(news);
        model.addAttribute("news", newsRepository.findAll());
        return "news/listNews";
    }
    
    @GetMapping("edit/{id}")
    public String showNewsFormToUpdate(@PathVariable("id") long id, Model model) {
    	News news = newsRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid news Id:" + id));
    	
        model.addAttribute("news", news);
        
        return "news/updateNews";
    }
    @PostMapping("edit/{id}")
    public String updateNews(@PathVariable("id") long id, @Valid News news, BindingResult result,
        Model model, @RequestParam("files") MultipartFile[] files) {
        if (result.hasErrors()) {
        	news.setId(id);
            return "news/updateNews";
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
			news.setPhoto(fileName.toString());
    	} 
	  
		    newsRepository.save(news);
        model.addAttribute("news", newsRepository.findAll());
        return "news/listNews";
    }

    @GetMapping("show/{id}")
    public String showNewsDetails(@PathVariable("id") long id, Model model) {
    	News neww = newsRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid news Id:" + id));
    	
        model.addAttribute("neww", neww);
        
        return "news/showNews";
    }

}
