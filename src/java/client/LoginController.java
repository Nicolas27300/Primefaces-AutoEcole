package client;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import metier.MetierFactory;
import metier.User;
import metier.UserService;

@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private UserService userSrv;
    private String adresse;
    private int cp;
    private String email;
    private String mdp;
    private boolean moniteur = false;
    private String nom;
    private String prenom;
    private String pseudo;
    private String ville;
    boolean logged = false;

    @PostConstruct
    public void init() {
        try {
            this.userSrv = MetierFactory.getUserService();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public boolean isMoniteur() {
        return moniteur;
    }

    public void setMoniteur(boolean moniteur) {
        this.moniteur = moniteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void add(ActionEvent event) {
        User user = new User();
        user.setAdresse(this.adresse);
        user.setCp(this.cp);
        user.setEmail(this.email);
        String mdp = this.encodeMd5(this.mdp);
        user.setMdp(mdp);
        user.setMoniteur(false);
        user.setNom(this.nom);
        user.setPrenom(this.prenom);
        user.setPseudo(this.pseudo);
        user.setVille(this.ville);
        try {
            if (this.userSrv.getByEmail(this.email).isEmpty() && this.userSrv.getByPseudoUtilise(this.pseudo).isEmpty()){
                this.userSrv.add(user);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Bravo, tu es inscris à l'auto école"));
            }
            else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Pseudo ou email déjà utilisé"));
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connexion(ActionEvent event) {
        try {
            User user = userSrv.getByPseudo(this.pseudo);
            if ((user != null) && (user.getMdp().equals(this.encodeMd5(this.mdp)))) {
                this.logged = true;
                this.moniteur = user.isMoniteur();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Tu es connecté"));
                ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
                content.redirect("http://localhost:8084/AutoEcole/");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Pseudo ou mot de passe incorrect"));
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deconnexion(ActionEvent event) throws IOException {
        this.logged = false;
        ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
        content.redirect("http://localhost:8084/AutoEcole/");
    }

    public String encodeMd5(String mdp) {
        byte[] uniqueKey = mdp.getBytes();
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }
}
