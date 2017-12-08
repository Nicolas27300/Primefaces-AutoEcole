package client;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import metier.MetierFactory;
import metier.Voiture;
import metier.VoitureService;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@ViewScoped
public class VoitureController implements Serializable {
    
    private VoitureService voitureSrv;
    private List<Voiture> voitures;
    private String immatriculation;
    private String modele;
    
    @PostConstruct
    public void init(){
        try {
            this.voitureSrv = MetierFactory.getVoitureService();
            this.voitures = this.voitureSrv.getAll();
        } catch (Exception ex) {
            Logger.getLogger(VoitureController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void add(){
        Voiture voiture = new Voiture();
        voiture.setImatriculation(this.immatriculation);
        voiture.setModele(this.modele);
        try {
            this.voitureSrv.add(voiture);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "La voiture a été ajouté"));
        } catch (Exception ex) {
            Logger.getLogger(VoitureController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowEdit(RowEditEvent event){
        Voiture user = (Voiture) event.getObject();
        try {
            this.voitureSrv.update(user);
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<Voiture> voitures) {
        this.voitures = voitures;
    }
    
    
    
    
    
}
