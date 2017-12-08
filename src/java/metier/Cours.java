package metier;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class Cours implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date heureDebut;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date heureFin;
    @OneToOne
    User eleve;
    @OneToOne
    User prof;
    @OneToOne
    Voiture voiture;
    int valide; // 0 = en cours 1 = ok 2 = pas ok

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public User getProf() {
        return prof;
    }

    public void setProf(User prof) {
        this.prof = prof;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public int isValide() {
        return valide;
    }

    public void setValide(int valide) {
        this.valide = valide;
    }

    public User getEleve() {
        return eleve;
    }

    public void setEleve(User eleve) {
        this.eleve = eleve;
    }
    
    
    
    
    
    
    
    
}
