package fr.limoges.valadon.testai.heritage.repository;

import fr.limoges.valadon.testai.heritage.entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}