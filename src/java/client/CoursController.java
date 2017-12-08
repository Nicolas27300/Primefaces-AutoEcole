package client;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import metier.Cours;
import metier.CoursService;
import metier.MetierFactory;
import metier.User;
import metier.UserService;
import metier.Voiture;
import metier.VoitureService;
import client.LoginController;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.eclipse.persistence.jpa.jpql.parser.ValueExpression;
import org.eclipse.persistence.sessions.Session;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

@ManagedBean
@ViewScoped
public class CoursController implements Serializable {

    CoursService coursSrv;
    UserService userSrv;
    VoitureService voitureSrv;
    Date debutCours;
    Date finCours;
    String moniteur;
    List<Cours> nonvalide;
    List<Cours> nextCours;
    List<Cours> nextCoursAttente;
    LoginController login;
    ScheduleModel eventMode;
    ScheduleModel eventModeProf;

    @PostConstruct
    public void init() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            login = (LoginController) session.getAttribute("loginController");
            this.coursSrv = MetierFactory.getCoursService();
            this.userSrv = MetierFactory.getUserService();
            this.voitureSrv = MetierFactory.getVoitureService();
            this.nonvalide = this.coursSrv.getByNonValide(this.userSrv.getByPseudo(this.login.getPseudo()));
            this.nextCours = this.coursSrv.getByPseudoNext(this.login.getPseudo());
            this.nextCoursAttente = this.coursSrv.getByPseudoNextAttente(this.login.getPseudo());
            this.eventMode = new DefaultScheduleModel();
            this.eventModeProf = new DefaultScheduleModel();
            this.loadAgenda();
            this.loadAgendaProf();
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getDebutCours() {
        return debutCours;
    }

    public void setDebutCours(Date debutCours) {
        this.debutCours = debutCours;
    }

    public Date getFinCours() {
        return finCours;
    }

    public void setFinCours(Date finCours) {
        this.finCours = finCours;
    }

    public String getMoniteur() {
        return moniteur;
    }

    public void setMoniteur(String moniteur) {
        this.moniteur = moniteur;
    }

    public List<Cours> getNonvalide() {
        return nonvalide;
    }

    public void setNonvalide(List<Cours> nonvalide) {
        this.nonvalide = nonvalide;
    }

    public List<Cours> getNextCours() {
        return nextCours;
    }

    public void setNextCours(List<Cours> nextCours) {
        this.nextCours = nextCours;
    }

    public List<Cours> getNextCoursAttente() {
        return nextCoursAttente;
    }

    public void setNextCoursAttente(List<Cours> nextCoursAttente) {
        this.nextCoursAttente = nextCoursAttente;
    }

    public ScheduleModel getEventMode() {
        return eventMode;
    }

    public void setEventMode(ScheduleModel eventMode) {
        this.eventMode = eventMode;
    }

    public ScheduleModel getEventModeProf() {
        return eventModeProf;
    }

    public void setEventModeProf(ScheduleModel eventModeProf) {
        this.eventModeProf = eventModeProf;
    }

    public void demandeCours(ActionEvent event) {
        Cours cours = new Cours();
        int i;
        boolean okVoiture = false;
        Voiture voiture = null;
        cours.setHeureDebut(this.debutCours);
        this.finCours = new Date(this.debutCours.getTime() + 1000 * 60 * 60);
        cours.setHeureFin(this.finCours);
        try {
            User prof = this.userSrv.getByPseudo(this.moniteur);
            User eleve = this.userSrv.getByPseudo(this.login.getPseudo());
            List<Voiture> allVoiture = this.voitureSrv.getAll();
            List<Cours> listVoituresNonDispo = this.coursSrv.getByDateVoiture(this.debutCours, this.finCours);
            if (listVoituresNonDispo.isEmpty()) {
                voiture = allVoiture.get(0);
                okVoiture = true;
            } else {
                for (i = 0; i < listVoituresNonDispo.size(); i++) {
                    if (!listVoituresNonDispo.get(i).getVoiture().getImatriculation().equals(allVoiture.get(i).getImatriculation())) {
                        voiture = allVoiture.get(i);
                        okVoiture = true;
                        break;
                    }
                }
                if (i != allVoiture.size()){
                    voiture = allVoiture.get(i);
                    okVoiture = true;
                }
            }
            if (okVoiture) {
                cours.setVoiture(voiture);
                cours.setValide(0);
                cours.setProf(prof);
                cours.setEleve(eleve);
                List<Cours> listCours = this.coursSrv.getByDateMoniteur(cours);
                if (listCours.isEmpty()) {
                    this.coursSrv.add(cours);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Demande de cours envoyé. Elle sera traitée le plus rapidement possible."));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "La demande sur cette date est impossible"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Aucune voiture de disponible sur cette date"));
            }

        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void accepterCours(ActionEvent event) {
        Cours cours = (Cours) event.getComponent().getAttributes().get("cours");
        try {
            List<Cours> listCours = this.coursSrv.getByDateMoniteur(cours);
            if (listCours.isEmpty()) {
                cours.setValide(1);
                this.coursSrv.update(cours);
                ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
                content.redirect("http://localhost:8084/AutoEcole/faces/gestioncours.xhtml");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Vous avez déjà accepté un cours sur cette date"));
            }
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refuserCours(ActionEvent event) {
        Cours cours = (Cours) event.getComponent().getAttributes().get("cours");
        cours.setValide(2);
        try {
            this.coursSrv.update(cours);
            ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
            content.redirect("http://localhost:8084/AutoEcole/faces/gestioncours.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void annulerCours(ActionEvent event) {
        Cours cours = (Cours) event.getComponent().getAttributes().get("cours");
        cours.setValide(2);
        try {
            this.coursSrv.update(cours);
            ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
            content.redirect("http://localhost:8084/AutoEcole/faces/profil.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAgenda() {
        try {
            List<Cours> listCours = this.coursSrv.getByCoursValide();
            for (int i = 0; i < listCours.size(); i++) {
                this.eventMode.addEvent(new DefaultScheduleEvent("Cours par : " + listCours.get(i).getProf().getPrenom() + " " + listCours.get(i).getProf().getNom(), listCours.get(i).getHeureDebut(), listCours.get(i).getHeureFin()));
            }
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadAgendaProf() {
        try {
            User prof = this.userSrv.getByPseudo(this.login.getPseudo());
            if (prof.isMoniteur()) {
                List<Cours> listCours = this.coursSrv.getByCoursValideByProf(prof);
                for (int i = 0; i < listCours.size(); i++) {
                    this.eventModeProf.addEvent(new DefaultScheduleEvent("Eleve : " + listCours.get(i).getEleve().getPrenom() + " " + listCours.get(i).getEleve().getNom() + " / Voiture : " + listCours.get(i).getVoiture().getImatriculation() + " (" + listCours.get(i).getVoiture().getModele() + ")", listCours.get(i).getHeureDebut(), listCours.get(i).getHeureFin()));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
