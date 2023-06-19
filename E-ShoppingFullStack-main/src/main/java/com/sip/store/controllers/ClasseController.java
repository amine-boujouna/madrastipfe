package com.sip.store.controllers;

import com.sip.store.entities.Article;
import com.sip.store.entities.Classe;
import com.sip.store.entities.Departement;
import com.sip.store.entities.Niveau;
import com.sip.store.repositories.ClasseRepository;
import com.sip.store.repositories.DepartementRepository;
import com.sip.store.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Controller
@RequestMapping("/classe/")
public class ClasseController {
    private final NiveauRepository niveauRepository;
    private final ClasseRepository classeRepository;
    @Autowired
    public ClasseController(NiveauRepository niveauRepository,ClasseRepository classeRepository) {
        this.niveauRepository = niveauRepository;
        this.classeRepository = classeRepository;
    }

    @GetMapping("list")
    public String listClasse(Model model) {
        model.addAttribute("classe", classeRepository.findAll());
        return "classe/listclasse";
    }
    @GetMapping("add")
    public String showAddClasseForm(Model model) {

        model.addAttribute("niveau", niveauRepository.findAll());
        model.addAttribute("classe", new Classe());
        return "classe/addClasse";
    }


    @PostMapping("add")
    //@ResponseBody
    public String addClasse(@Valid Classe classe, @RequestParam(name = "niveauId", required = false) Long d) {

        Niveau niveau = niveauRepository.findById(d)
                .orElseThrow(()-> new IllegalArgumentException("Invalid niveau Id:" + d));
        classe.setNiveau(niveau);
        classeRepository.save(classe);
        return "redirect:list";

    }
    @GetMapping("delete/{id}")
    public String deleteClasse(@PathVariable("id") long id, Model model) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Invalid classe Id:" + id));
        classeRepository.delete(classe);
        model.addAttribute("classe", classeRepository.findAll());
        return "classe/listclasse";
    }

    @GetMapping("edit/{id}")
    public String showClasseFormToUpdate(@PathVariable("id") long id, Model model) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid classe Id:" + id));

        model.addAttribute("classe", classe);
        model.addAttribute("niveau", niveauRepository.findAll());
        model.addAttribute("idNiveau", classe.getNiveau().getId());

        return "classe/updateClasse";
    }
    @PostMapping("edit/{id}")
    public String updateClasse(@PathVariable("id") long id, @Valid Classe classe, BindingResult result,
                               Model model, @RequestParam(name = "idNiveau", required = false) Long p) {
        if (result.hasErrors()) {
            classe.setId(id);
            return "classe/updateClasse";
        }
        Niveau niveau = niveauRepository.findById(p)
                .orElseThrow(()-> new IllegalArgumentException("Invalid niveau Id:" + p));
        classe.setNiveau(niveau);
        classeRepository.save(classe);
        model.addAttribute("classe", classeRepository.findAll());
        return "classe/listclasse";
    }

    @GetMapping("show/{id}")
    public String showClasseDetails(@PathVariable("id") long id, Model model) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid classe Id:" + id));

        model.addAttribute("classe", classe);

        return "classe/showClasse";
    }


}