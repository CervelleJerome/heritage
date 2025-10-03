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
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final CoursRepository coursRepository;
    private final EnseignantCoursRepository enseignantCoursRepository;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository, CoursRepository coursRepository,
                            EnseignantCoursRepository enseignantCoursRepository) {
        this.enseignantRepository = enseignantRepository;
        this.coursRepository = coursRepository;
        this.enseignantCoursRepository = enseignantCoursRepository;
    }

    public List<Enseignant> findAll() {
        return enseignantRepository.findAll();
    }

    public Optional<Enseignant> findById(Long id) {
        return enseignantRepository.findById(id);
    }

    public Enseignant save(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    public void deleteById(Long id) {
        enseignantRepository.deleteById(id);
    }

    @Transactional
    public void addCoursToEnseignant(Long enseignantId, Long coursId, int heuresDeCours) {
        Optional<Enseignant> enseignantOpt = enseignantRepository.findById(enseignantId);
        Optional<Cours> coursOpt = coursRepository.findById(coursId);

        if (enseignantOpt.isPresent() && coursOpt.isPresent()) {
            Enseignant enseignant = enseignantOpt.get();
            Cours cours = coursOpt.get();

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
                enseignant.getEnseignantCours().add(enseignantCours);
                cours.getEnseignantCours().add(enseignantCours);

                // Save the entities to ensure they're properly managed
                enseignantRepository.save(enseignant);
                coursRepository.save(cours);
            }
        }
    }

    @Transactional
    public void addCoursToEnseignant(Long enseignantId, Long coursId) {
        // Default to 0 hours if not specified
        addCoursToEnseignant(enseignantId, coursId, 0);
    }

    @Transactional
    public void updateHeuresDeCours(Long enseignantId, Long coursId, int heuresDeCours) {
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
    public void removeCoursFromEnseignant(Long enseignantId, Long coursId) {
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

    public Set<Cours> getCoursByEnseignant(Long enseignantId) {
        Optional<Enseignant> enseignantOpt = enseignantRepository.findById(enseignantId);
        return enseignantOpt.map(Enseignant::getCours).orElse(null);
    }

    public List<EnseignantCours> getEnseignantCoursByEnseignant(Long enseignantId) {
        return enseignantCoursRepository.findByEnseignantId(enseignantId);
    }
}
