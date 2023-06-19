package com.sip.store.controllers;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.sip.store.entities.Article;
import com.sip.store.entities.Messaging;
import com.sip.store.entities.Provider;
import com.sip.store.entities.Role;
import com.sip.store.entities.User;
import com.sip.store.repositories.ArticleRepository;
import com.sip.store.repositories.MessagingRepository;
import com.sip.store.repositories.ProviderRepository;
import com.sip.store.repositories.RoleRepository;
import com.sip.store.repositories.UserRepository;
import com.sip.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class LoginController {
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final MessagingRepository messagingRepository;
	 @Autowired
	    public LoginController(ArticleRepository articleRepository, ProviderRepository providerRepository,UserRepository userRepository, RoleRepository roleRepository, MessagingRepository messagingRepository) {
	        this.articleRepository = articleRepository;
	        this.providerRepository = providerRepository;
	        this.userRepository = userRepository;
	        this.roleRepository = roleRepository;
	        this.messagingRepository = messagingRepository;
	    }
	    
    @Autowired
    private UserService userService;
    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    
    @RequestMapping(value={"/dashboard"}, method = RequestMethod.GET)
    public ModelAndView goToDashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
    	long nbrArticles =  articleRepository.count();
    	long nbrProviders =  providerRepository.count();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        //System.out.println(user.getEmail()+" "+user.getName()+" "+user.getLastName());
    	List<Article> articles = articleRepository.findProductSoldOut();
    	long nbrSoldOutProduct =  articles.size();
    	model.addAttribute("nbrArticles", nbrArticles);
    	model.addAttribute("nbrProviders", nbrProviders);
    	model.addAttribute("nbrSoldOutProduct", nbrSoldOutProduct);
    	model.addAttribute("users", userRepository.findAll());
    	long nbrMsgNonLus = (messagingRepository.findAllMessageByStatus("Non lu",user)).size();
    	model.addAttribute("nbrMsgNonLus", nbrMsgNonLus );
    	// System.out.println(nbrMsgNonLus);
    	model.addAttribute("articles", articleRepository.findAll());
    	
    	List<Messaging> messagesUnread = messagingRepository.findAllMessageByStatus("Non lu",user);
    	
   
    	model.addAttribute("messagesUnread", messagesUnread);
        modelAndView.setViewName("dashboard/dashboard");
        return modelAndView;
    }
    @RequestMapping(value={"/home"}, method = RequestMethod.GET)
    public ModelAndView accueil(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        ///
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getEmail()+" "+user.getName()+" "+user.getLastName());
        ////

        return modelAndView;
    }
    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
    	
        ModelAndView modelAndView = new ModelAndView();
        
        User userExists = userService.findUserByEmail(user.getEmail());
        
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        }
        
        else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }
   /* @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }*/
    
    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
}

