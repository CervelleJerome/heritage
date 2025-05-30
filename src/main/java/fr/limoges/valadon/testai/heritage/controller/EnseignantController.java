package fr.limoges.valadon.testai.heritage.controller;

import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import fr.limoges.valadon.testai.heritage.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    private final EnseignantService enseignantService;

    @Autowired
    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String listEnseignants(Model model) {
        model.addAttribute("enseignants", enseignantService.findAll());
        return "enseignants/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enseignant", new Enseignant());
        return "enseignants/form";
    }

    @PostMapping
    public String saveEnseignant(@ModelAttribute Enseignant enseignant) {
        enseignantService.save(enseignant);
        return "redirect:/enseignants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Enseignant> enseignant = enseignantService.findById(id);
        if (enseignant.isPresent()) {
            model.addAttribute("enseignant", enseignant.get());
            return "enseignants/form";
        }
        return "redirect:/enseignants";
    }

    @GetMapping("/{id}/delete")
    public String deleteEnseignant(@PathVariable Long id) {
        enseignantService.deleteById(id);
        return "redirect:/enseignants";
    }
}