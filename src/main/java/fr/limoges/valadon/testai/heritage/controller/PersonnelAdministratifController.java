package fr.limoges.valadon.testai.heritage.controller;

import fr.limoges.valadon.testai.heritage.entity.PersonnelAdministratif;
import fr.limoges.valadon.testai.heritage.service.PersonnelAdministratifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/personnels")
public class PersonnelAdministratifController {

    private final PersonnelAdministratifService personnelAdministratifService;

    @Autowired
    public PersonnelAdministratifController(PersonnelAdministratifService personnelAdministratifService) {
        this.personnelAdministratifService = personnelAdministratifService;
    }

    @GetMapping
    public String listPersonnels(Model model) {
        model.addAttribute("personnels", personnelAdministratifService.findAll());
        return "personnels/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("personnel", new PersonnelAdministratif());
        return "personnels/form";
    }

    @PostMapping
    public String savePersonnel(@ModelAttribute PersonnelAdministratif personnel) {
        personnelAdministratifService.save(personnel);
        return "redirect:/personnels";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<PersonnelAdministratif> personnel = personnelAdministratifService.findById(id);
        if (personnel.isPresent()) {
            model.addAttribute("personnel", personnel.get());
            return "personnels/form";
        }
        return "redirect:/personnels";
    }

    @GetMapping("/{id}/delete")
    public String deletePersonnel(@PathVariable Long id) {
        personnelAdministratifService.deleteById(id);
        return "redirect:/personnels";
    }
}