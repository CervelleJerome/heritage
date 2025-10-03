package fr.limoges.valadon.testai.heritage.service;

import fr.limoges.valadon.testai.heritage.entity.Cours;
import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import fr.limoges.valadon.testai.heritage.entity.EnseignantCours;
import fr.limoges.valadon.testai.heritage.repository.CoursRepository;
import fr.limoges.valadon.testai.heritage.repository.EnseignantCoursRepository;
import fr.limoges.valadon.testai.heritage.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CoursService {

    private final CoursRepository coursRepository;
    private final EnseignantRepository enseignantRepository;
    private final EnseignantCoursRepository enseignantCoursRepository;

    @Autowired
    public CoursService(CoursRepository coursRepository, EnseignantRepository enseignantRepository, 
                        EnseignantCoursRepository enseignantCoursRepository) {
        this.coursRepository = coursRepository;
        this.enseignantRepository = enseignantRepository;
        this.enseignantCoursRepository = enseignantCoursRepository;
    }

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    public Optional<Cours> findById(Long id) {
        return coursRepository.findById(id);
    }

    public Cours save(Cours cours) {
        return coursRepository.save(cours);
    }

    public void deleteById(Long id) {
        coursRepository.deleteById(id);
    }

    @Transactional
    public void addEnseignantToCours(Long coursId, Long enseignantId, int heuresDeCours) {
        Optional<Cours> coursOpt = coursRepository.findById(coursId);
        Optional<Enseignant> enseignantOpt = enseignantRepository.findById(enseignantId);

        if (coursOpt.isPresent() && enseignantOpt.isPresent()) {
            Cours cours = coursOpt.get();
            Enseignant enseignant = enseignantOpt.get();

            // Check if the relationship already exists
            EnseignantCours existingRelation = enseignantCoursRepository.findByEnseignantIdAndCoursId(enseignantId, coursId);
            if (existingRelation != null) {
                // Update hours if the relationship already exists
                existingRelation.setHeuresDeCours(heuresDeCours);
                enseignantCoursRepository.save(existingRelation);
            } else {
                // Create a new relationship
                EnseignantCours enseignantCours = new EnseignantCours(enseignant, cours, heuresDeCours);

                // Update collections for consistency
                cours.getEnseignantCours().add(enseignantCours);
                enseignant.getEnseignantCours().add(enseignantCours);

                // Save the entities to ensure they're properly managed
                coursRepository.save(cours);
                enseignantRepository.save(enseignant);
            }
        }
    }

    @Transactional
    public void addEnseignantToCours(Long coursId, Long enseignantId) {
        // Default to 0 hours if not specified
        addEnseignantToCours(coursId, enseignantId, 0);
    }

    @Transactional
    public void updateHeuresDeCours(Long coursId, Long enseignantId, int heuresDeCours) {
        EnseignantCours enseignantCours = enseignantCoursRepository.findByEnseignantIdAndCoursId(enseignantId, coursId);
        if (enseignantCours != null) {
            enseignantCours.setHeuresDeCours(heuresDeCours);

            // Get the related entities to ensure they're properly managed
            Enseignant enseignant = enseignantCours.getEnseignant();
            Cours cours = enseignantCours.getCours();

            // Save the entities to ensure they're properly managed
            if (enseignant != null) {
                enseignantRepository.save(enseignant);
            }

            if (cours != null) {
                coursRepository.save(cours);
            }
        }
    }

    @Transactional
    public void removeEnseignantFromCours(Long coursId, Long enseignantId) {
        EnseignantCours enseignantCours = enseignantCoursRepository.findByEnseignantIdAndCoursId(enseignantId, coursId);
        if (enseignantCours != null) {
            // Update collections for consistency
            Enseignant enseignant = enseignantCours.getEnseignant();
            Cours cours = enseignantCours.getCours();

            if (enseignant != null) {
                enseignant.getEnseignantCours().remove(enseignantCours);
                enseignantRepository.save(enseignant);
            }

            if (cours != null) {
                cours.getEnseignantCours().remove(enseignantCours);
                coursRepository.save(cours);
            }

            // Clear references to prevent issues with managed entities
            enseignantCours.setEnseignant(null);
            enseignantCours.setCours(null);

            // Delete the relationship
            enseignantCoursRepository.delete(enseignantCours);
        }
    }

    public Set<Enseignant> getEnseignantsByCours(Long coursId) {
        Optional<Cours> coursOpt = coursRepository.findById(coursId);
        return coursOpt.map(Cours::getEnseignants).orElse(null);
    }

    public List<EnseignantCours> getEnseignantCoursByCours(Long coursId) {
        return enseignantCoursRepository.findByCoursId(coursId);
    }
}
