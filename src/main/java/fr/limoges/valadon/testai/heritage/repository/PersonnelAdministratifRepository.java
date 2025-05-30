package fr.limoges.valadon.testai.heritage.repository;

import fr.limoges.valadon.testai.heritage.entity.PersonnelAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelAdministratifRepository extends JpaRepository<PersonnelAdministratif, Long> {
}