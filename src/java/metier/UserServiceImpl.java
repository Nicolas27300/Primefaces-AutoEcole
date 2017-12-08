package metier;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import lml.persistence.jpa.AbstracCrudServiceJPA;

public class UserServiceImpl extends AbstracCrudServiceJPA<User> implements UserService {

    UserServiceImpl(String PU) {
        super(PU);
    }

    @Override
    public User getByPseudo(String pseudo) throws Exception {
        User user = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM User m WHERE m.pseudo = :fpseudo");
            query.setParameter("fpseudo", pseudo);
            user = (User) query.getSingleResult();
        } finally {
            this.close();
        }
        return user;
    }

    @Override
    public List<User> getByMoniteur() throws Exception {
        List users = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM User m WHERE m.moniteur = true");
            users = query.getResultList();
        } finally {
            this.close();
        }
        return users;
    }

    @Override
    public List<User> getByEmail(String email) throws Exception {
        List<User> listUsers = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM User m WHERE m.email = :email");
            query.setParameter("email", email);
            listUsers = query.getResultList();
        } finally {
            this.close();
        }
        return listUsers;
    }
    
    @Override
    public List<User> getByPseudoUtilise(String pseudo) throws Exception {
        List<User> listUsers = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM User m WHERE m.pseudo = :pseudo");
            query.setParameter("pseudo", pseudo);
            listUsers = query.getResultList();
        } finally {
            this.close();
        }
        return listUsers;
    }

}
