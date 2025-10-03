package fr.limoges.valadon.testai.heritage.controller;

import fr.limoges.valadon.testai.heritage.entity.Cours;
import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import fr.limoges.valadon.testai.heritage.entity.EnseignantCours;
import fr.limoges.valadon.testai.heritage.service.CoursService;
import fr.limoges.valadon.testai.heritage.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/cours")
public class CoursController {

    private final CoursService coursService;
    private final EnseignantService enseignantService;

    @Autowired
    public CoursController(CoursService coursService, EnseignantService enseignantService) {
        this.coursService = coursService;
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String listCours(Model model) {
        model.addAttribute("cours", coursService.findAll());
        return "cours/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("enseignants", enseignantService.findAll());
        return "cours/form";
    }

    @PostMapping
    public String saveCours(@ModelAttribute Cours cours, @RequestParam(required = false) List<Long> enseignantIds) {
        Cours savedCours = coursService.save(cours);

        if (enseignantIds != null) {
            for (Long enseignantId : enseignantIds) {
                coursService.addEnseignantToCours(savedCours.getId(), enseignantId);
            }
        }

        return "redirect:/cours";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Cours> cours = coursService.findById(id);
        if (cours.isPresent()) {
            model.addAttribute("cours", cours.get());
            model.addAttribute("enseignants", enseignantService.findAll());
            model.addAttribute("selectedEnseignants", cours.get().getEnseignants());
            return "cours/form";
        }
        return "redirect:/cours";
    }

    @GetMapping("/{id}/delete")
    public String deleteCours(@PathVariable Long id) {
        coursService.deleteById(id);
        return "redirect:/cours";
    }

    @GetMapping("/{id}/enseignants")
    public String viewEnseignants(@PathVariable Long id, Model model) {
        Optional<Cours> cours = coursService.findById(id);
        if (cours.isPresent()) {
            model.addAttribute("cours", cours.get());
            model.addAttribute("enseignants", cours.get().getEnseignants());
            model.addAttribute("availableEnseignants", enseignantService.findAll());

            // Get the EnseignantCours relationships for this course
            List<EnseignantCours> enseignantCoursList = coursService.getEnseignantCoursByCours(id);
            model.addAttribute("enseignantCoursList", enseignantCoursList);

            // Create a map of enseignantId to heuresDeCours for easier access in the template
            java.util.Map<Long, Integer> heuresDeCoursMap = new java.util.HashMap<>();
            for (EnseignantCours ec : enseignantCoursList) {
                heuresDeCoursMap.put(ec.getEnseignant().getId(), ec.getHeuresDeCours());
            }
            model.addAttribute("heuresDeCoursMap", heuresDeCoursMap);

            return "cours/enseignants";
        }
        return "redirect:/cours";
    }

    @PostMapping("/{coursId}/enseignants/add")
    public String addEnseignantToCours(
            @PathVariable Long coursId, 
            @RequestParam Long enseignantId,
            @RequestParam(required = false, defaultValue = "0") Integer heuresDeCours) {

        coursService.addEnseignantToCours(coursId, enseignantId, heuresDeCours);
        return "redirect:/cours/" + coursId + "/enseignants";
    }

    @PostMapping("/{coursId}/enseignants/{enseignantId}/updateHours")
    public String updateHeuresDeCours(
            @PathVariable Long coursId,
            @PathVariable Long enseignantId,
            @RequestParam Integer heuresDeCours) {

        coursService.updateHeuresDeCours(coursId, enseignantId, heuresDeCours);
        return "redirect:/cours/" + coursId + "/enseignants";
    }

    @GetMapping("/{coursId}/enseignants/{enseignantId}/remove")
    public String removeEnseignantFromCours(@PathVariable Long coursId, @PathVariable Long enseignantId) {
        coursService.removeEnseignantFromCours(coursId, enseignantId);
        return "redirect:/cours/" + coursId + "/enseignants";
    }
}
