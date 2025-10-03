package fr.limoges.valadon.testai.heritage.controller;

import com.itextpdf.text.DocumentException;
import fr.limoges.valadon.testai.heritage.entity.Cours;
import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import fr.limoges.valadon.testai.heritage.entity.EnseignantCours;
import fr.limoges.valadon.testai.heritage.service.CoursService;
import fr.limoges.valadon.testai.heritage.service.EnseignantService;
import fr.limoges.valadon.testai.heritage.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    private final EnseignantService enseignantService;
    private final CoursService coursService;
    private final PdfService pdfService;

    @Autowired
    public EnseignantController(EnseignantService enseignantService, CoursService coursService, PdfService pdfService) {
        this.enseignantService = enseignantService;
        this.coursService = coursService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String listEnseignants(Model model) {
        model.addAttribute("enseignants", enseignantService.findAll());
        return "enseignants/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enseignant", new Enseignant());
        model.addAttribute("cours", coursService.findAll());
        return "enseignants/form";
    }

    @PostMapping
    public String saveEnseignant(@ModelAttribute Enseignant enseignant, @RequestParam(required = false) List<Long> coursIds) {
        Enseignant savedEnseignant = enseignantService.save(enseignant);

        if (coursIds != null) {
            for (Long coursId : coursIds) {
                enseignantService.addCoursToEnseignant(savedEnseignant.getId(), coursId);
            }
        }

        return "redirect:/enseignants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Enseignant> enseignant = enseignantService.findById(id);
        if (enseignant.isPresent()) {
            model.addAttribute("enseignant", enseignant.get());
            model.addAttribute("cours", coursService.findAll());
            model.addAttribute("selectedCours", enseignant.get().getCours());
            return "enseignants/form";
        }
        return "redirect:/enseignants";
    }

    @GetMapping("/{id}/delete")
    public String deleteEnseignant(@PathVariable Long id) {
        enseignantService.deleteById(id);
        return "redirect:/enseignants";
    }

    @GetMapping("/{id}/cours")
    public String viewCours(@PathVariable Long id, Model model) {
        Optional<Enseignant> enseignant = enseignantService.findById(id);
        if (enseignant.isPresent()) {
            model.addAttribute("enseignant", enseignant.get());
            model.addAttribute("cours", enseignant.get().getCours());
            model.addAttribute("availableCours", coursService.findAll());

            // Get the EnseignantCours relationships for this enseignant
            List<EnseignantCours> enseignantCoursList = enseignantService.getEnseignantCoursByEnseignant(id);
            model.addAttribute("enseignantCoursList", enseignantCoursList);

            // Create a map of coursId to heuresDeCours for easier access in the template
            java.util.Map<Long, Integer> heuresDeCoursMap = new java.util.HashMap<>();
            for (EnseignantCours ec : enseignantCoursList) {
                heuresDeCoursMap.put(ec.getCours().getId(), ec.getHeuresDeCours());
            }
            model.addAttribute("heuresDeCoursMap", heuresDeCoursMap);

            return "enseignants/cours";
        }
        return "redirect:/enseignants";
    }

    @PostMapping("/{enseignantId}/cours/add")
    public String addCoursToEnseignant(
            @PathVariable Long enseignantId, 
            @RequestParam Long coursId,
            @RequestParam(required = false, defaultValue = "0") Integer heuresDeCours) {

        enseignantService.addCoursToEnseignant(enseignantId, coursId, heuresDeCours);
        return "redirect:/enseignants/" + enseignantId + "/cours";
    }

    @PostMapping("/{enseignantId}/cours/{coursId}/updateHours")
    public String updateHeuresDeCours(
            @PathVariable Long enseignantId,
            @PathVariable Long coursId,
            @RequestParam Integer heuresDeCours) {

        enseignantService.updateHeuresDeCours(enseignantId, coursId, heuresDeCours);
        return "redirect:/enseignants/" + enseignantId + "/cours";
    }

    @GetMapping("/{enseignantId}/cours/{coursId}/remove")
    public String removeCoursFromEnseignant(@PathVariable Long enseignantId, @PathVariable Long coursId) {
        enseignantService.removeCoursFromEnseignant(enseignantId, coursId);
        return "redirect:/enseignants/" + enseignantId + "/cours";
    }

    /**
     * Generates and returns a PDF document with the teacher's course information
     * @param enseignantId The ID of the teacher
     * @return A ResponseEntity containing the PDF document
     */
    @GetMapping("/{enseignantId}/pdf")
    public ResponseEntity<byte[]> downloadEnseignantCoursePdf(@PathVariable Long enseignantId) {
        try {
            // Get the teacher to use their name in the filename
            Optional<Enseignant> enseignantOpt = enseignantService.findById(enseignantId);
            if (!enseignantOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Enseignant enseignant = enseignantOpt.get();
            String filename = "enseignant_" + enseignant.getNom() + "_" + enseignant.getPrenom() + "_cours.pdf";

            // Generate the PDF
            byte[] pdfBytes = pdfService.generateEnseignantCoursePdf(enseignantId);

            // Set up the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
