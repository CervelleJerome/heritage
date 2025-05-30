package fr.limoges.valadon.testai.heritage.controller;

import fr.limoges.valadon.testai.heritage.entity.Etudiant;
import fr.limoges.valadon.testai.heritage.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;

    @Autowired
    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public String listEtudiants(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiants/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "etudiants/form";
    }

    @PostMapping
    public String saveEtudiant(@ModelAttribute Etudiant etudiant) {
        etudiantService.save(etudiant);
        return "redirect:/etudiants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Etudiant> etudiant = etudiantService.findById(id);
        if (etudiant.isPresent()) {
            model.addAttribute("etudiant", etudiant.get());
            return "etudiants/form";
        }
        return "redirect:/etudiants";
    }

    @GetMapping("/{id}/delete")
    public String deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteById(id);
        return "redirect:/etudiants";
    }
}