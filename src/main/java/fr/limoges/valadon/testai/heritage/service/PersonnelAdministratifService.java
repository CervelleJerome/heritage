package fr.limoges.valadon.testai.heritage.service;

import fr.limoges.valadon.testai.heritage.entity.PersonnelAdministratif;
import fr.limoges.valadon.testai.heritage.repository.PersonnelAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelAdministratifService {

    private final PersonnelAdministratifRepository personnelAdministratifRepository;

    @Autowired
    public PersonnelAdministratifService(PersonnelAdministratifRepository personnelAdministratifRepository) {
        this.personnelAdministratifRepository = personnelAdministratifRepository;
    }

    public List<PersonnelAdministratif> findAll() {
        return personnelAdministratifRepository.findAll();
    }

    public Optional<PersonnelAdministratif> findById(Long id) {
        return personnelAdministratifRepository.findById(id);
    }

    public PersonnelAdministratif save(PersonnelAdministratif personnelAdministratif) {
        return personnelAdministratifRepository.save(personnelAdministratif);
    }

    public void deleteById(Long id) {
        personnelAdministratifRepository.deleteById(id);
    }
}