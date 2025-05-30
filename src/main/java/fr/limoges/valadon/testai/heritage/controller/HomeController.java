package fr.limoges.valadon.testai.heritage.controller;

import fr.limoges.valadon.testai.heritage.repository.EnseignantRepository;
import fr.limoges.valadon.testai.heritage.repository.EtudiantRepository;
import fr.limoges.valadon.testai.heritage.repository.PersonnelAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EnseignantRepository enseignantRepository;
    private final EtudiantRepository etudiantRepository;
    private final PersonnelAdministratifRepository personnelAdministratifRepository;

    @Autowired
    public HomeController(EnseignantRepository enseignantRepository, 
                          EtudiantRepository etudiantRepository, 
                          PersonnelAdministratifRepository personnelAdministratifRepository) {
        this.enseignantRepository = enseignantRepository;
        this.etudiantRepository = etudiantRepository;
        this.personnelAdministratifRepository = personnelAdministratifRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Count the number of entities in each repository
        long enseignantCount = enseignantRepository.count();
        long etudiantCount = etudiantRepository.count();
        long personnelCount = personnelAdministratifRepository.count();

        // Add the counts to the model
        model.addAttribute("enseignantCount", enseignantCount);
        model.addAttribute("etudiantCount", etudiantCount);
        model.addAttribute("personnelCount", personnelCount);

        return "index";
    }
}
