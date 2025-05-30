package fr.limoges.valadon.testai.heritage.repository;

import fr.limoges.valadon.testai.heritage.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
}