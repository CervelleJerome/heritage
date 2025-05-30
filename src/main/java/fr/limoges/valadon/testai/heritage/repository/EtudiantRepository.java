package fr.limoges.valadon.testai.heritage.repository;

import fr.limoges.valadon.testai.heritage.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
}