package client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIColumn;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import metier.MetierFactory;
import metier.User;
import metier.UserService;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@ViewScoped
public class UserController implements Serializable {
    
    private UserService userSrv;
    private List<User> users;
    private Map<String, String> moniteurs;
    private String moniteur;
    private int count;
    
    @PostConstruct
    public void init(){
        System.out.println("User.init()");
        try {
            this.userSrv = MetierFactory.getUserService();
            this.users = this.userSrv.getAll();
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }
    
    public void onRowEdit(RowEditEvent event){
        User user = (User) event.getObject();
        try {
            this.userSrv.update(user);
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public void removeUser(ActionEvent event){
        User user = (User) event.getComponent().getAttributes().get("user");
        try {
            this.userSrv.remove(user);
            ExternalContext content = FacesContext.getCurrentInstance().getExternalContext();
            content.redirect("http://localhost:8084/AutoEcole/faces/gestionusers.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CoursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, String> getMoniteurs() {
        this.moniteurs = new HashMap<String, String>();
        try {
            List<User> users = this.userSrv.getByMoniteur();
            for (int i = 0; i < users.size(); i++){
               this.moniteurs.put(users.get(i).getPseudo(), users.get(i).getPseudo());
            }
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return moniteurs;
    }

    public String getMoniteur() {
        return moniteur;
    }

    public void setMoniteur(String moniteur) {
        this.moniteur = moniteur;
    }
    
    
    
    
    
    
    
}
