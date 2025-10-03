package fr.limoges.valadon.testai.heritage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Enseignant extends Personne {
    private int heuresSupplementaires;
    private int heuresCours;

    @OneToMany(mappedBy = "enseignant", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<EnseignantCours> enseignantCours = new HashSet<>();

    // For backward compatibility
    public Set<Cours> getCours() {
        return enseignantCours.stream()
                .map(EnseignantCours::getCours)
                .collect(Collectors.toSet());
    }


}
