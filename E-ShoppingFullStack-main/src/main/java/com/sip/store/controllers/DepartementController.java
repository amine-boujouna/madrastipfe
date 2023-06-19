package com.sip.store.controllers;

import com.sip.store.entities.Article;
import com.sip.store.entities.Departement;
import com.sip.store.entities.Niveau;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.DepartementRepository;
import com.sip.store.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Controller
@RequestMapping("/departements/")
public class DepartementController {
    private final DepartementRepository departementRepository;

    @Autowired
    public DepartementController(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }



    @GetMapping("list")
    //@ResponseBody
    public String listProviders(Model model) {


        model.addAttribute("departements", departementRepository.findAll());

        return "departements/listDepartements";

    }

    @GetMapping("add")
    public String showAddDepartementForm(Model model) {
        Departement departement = new Departement();// object dont la valeur des attributs par defaut
        model.addAttribute("departement", departement);
        return "departements/addDepartement";
    }

    @PostMapping("add")
    public String addDepartement(@Valid Departement departement) {
        departementRepository.save(departement);
        return "redirect:list";
    }



    @GetMapping("delete/{id}")
    public String deleteDepartement(@PathVariable("id") long id, Model model) {

        Departement departement = departementRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid departement Id:" + id));

        System.out.println("suite du programme...");

        departementRepository.delete(departement);
        return "redirect:../list";
    }


    @GetMapping("edit/{id}")
    public String showProviderFormToUpdate(@PathVariable("id") long id, Model model) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid departement Id:" + id));

        model.addAttribute("departement", departement);

        return "departements/updateDepartement";
    }





    @PostMapping("update/{id}")
    public String updateProvider(@PathVariable("id") long id, @Valid Departement departement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            departement.setId(id);
            return "departements/updateDepartement";
        }
        departementRepository.save(departement);
        return "redirect:../list";

    }


    @GetMapping("show/{id}")
    public String showProvider(@PathVariable("id") long id, Model model) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
        List<Niveau> niveau = departementRepository.findNiveauByDepartement(id);
        for (Niveau a : niveau)
            System.out.println("Niveau = " + a.getNom());

        model.addAttribute("niveau", niveau);
        model.addAttribute("departement", departement);
        return "departements/showDepartement";
    }


}
