package com.sip.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping(value={"/","h**"})
    //@ResponseBody
    public String home() {
        return "front/index.html";
    }

    public String login() {
        return "";
    }

    public String registration() {
        return "";
    }

    public String forgotpassword() {
        return "";
    }


   /*@RequestMapping("/contact")
   public String contact() {
        return "front/contact.html";
    }*/
}
