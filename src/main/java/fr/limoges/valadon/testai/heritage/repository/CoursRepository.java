package fr.limoges.valadon.testai.heritage.repository;

import fr.limoges.valadon.testai.heritage.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
}