package com.sip.store.controllers;

import com.sip.store.entities.Article;
import com.sip.store.entities.Departement;
import com.sip.store.entities.Niveau;
import com.sip.store.repositories.DepartementRepository;
import com.sip.store.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/niveau/")
public class NiveauController {
    private final NiveauRepository niveauRepository;
    private final DepartementRepository departementRepository;
    @Autowired
    public NiveauController(NiveauRepository niveauRepository,DepartementRepository departementRepository) {
        this.niveauRepository = niveauRepository;
        this.departementRepository = departementRepository;
    }

    @GetMapping("list")
    public String listNiveau(Model model) {
        model.addAttribute("niveau", niveauRepository.findAll());
        return "niveau/listniveau";
    }
    @GetMapping("add")
    public String showAddNiveauForm(Article article, Model model) {

        model.addAttribute("departements", departementRepository.findAll());
        model.addAttribute("niveau", new Niveau());
        return "niveau/addNiveau";
    }


    @PostMapping("add")
    //@ResponseBody
    public String addNiveau(@Valid Niveau niveau,  @RequestParam(name = "departementId", required = false) Long d) {

        Departement departement = departementRepository.findById(d)
                .orElseThrow(()-> new IllegalArgumentException("Invalid departement Id:" + d));
        niveau.setDepartement(departement);
        niveauRepository.save(niveau);
        return "redirect:list";

    }
    @GetMapping("delete/{id}")
    public String deleteNiveau(@PathVariable("id") long id, Model model) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid niveau Id:" + id));
        niveauRepository.delete(niveau);
        model.addAttribute("niveau", niveauRepository.findAll());
        return "niveau/listniveau";
    }

    @GetMapping("edit/{id}")
    public String showNiveauFormToUpdate(@PathVariable("id") long id, Model model) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid niveau Id:" + id));

        model.addAttribute("niveau", niveau);
        model.addAttribute("departements", departementRepository.findAll());
        model.addAttribute("idDepartement", niveau.getDepartement().getId());

        return "niveau/updateNiveau";
    }
    @PostMapping("edit/{id}")
    public String updateNiveau(@PathVariable("id") long id, @Valid Niveau niveau,BindingResult result,
                               Model model, @RequestParam(name = "idDepartement", required = false) Long p) {
        if (result.hasErrors()) {
            niveau.setId(id);
            return "niveau/updateNiveau";
        }
        Departement departement = departementRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid departement Id:" + p));
        niveau.setDepartement(departement);
        niveauRepository.save(niveau);
        model.addAttribute("niveau", niveauRepository.findAll());
        return "niveau/listniveau";
    }

    @GetMapping("show/{id}")
    public String showArticleDetails(@PathVariable("id") long id, Model model) {
        Niveau niveau = niveauRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid niveau Id:" + id));

        model.addAttribute("niveau", niveau);

        return "niveau/showNiveau";
    }



}
