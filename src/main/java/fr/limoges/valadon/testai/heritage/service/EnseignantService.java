package fr.limoges.valadon.testai.heritage.service;

import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import fr.limoges.valadon.testai.heritage.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
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
}