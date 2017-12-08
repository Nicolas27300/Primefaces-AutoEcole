package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import lml.persistence.jpa.AbstracCrudServiceJPA;

public class CoursServiceImpl extends AbstracCrudServiceJPA<Cours> implements CoursService, Serializable {
    
    CoursServiceImpl(String PU){
        super(PU);
    }

    @Override
    public List<Cours> getByNonValide(User prof) throws Exception {
        List<Cours> cours = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.valide = 0 AND m.prof = :prof");
            query.setParameter("prof", prof);
            cours = query.getResultList();
        } finally {
            this.close();
        }
        return cours;
    }

    @Override
    public List<Cours> getByDateMoniteur(Cours cours) throws Exception {
        List <Cours> listCours = new ArrayList();
        try {
            this.open();
            //Query query = em.createQuery("SELECT m FROM Cours m WHERE m.prof.pseudo = :fprenommoniteur AND (m.valide = 1) AND (m.heureDebut BETWEEN :fdatedebut AND :fdatefin OR m.heureFin BETWEEN :fdatedebut AND :fdatefin)");
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.prof.pseudo = :fprenommoniteur AND (m.valide = 1) AND (m.heureDebut >= :fdatedebut AND m.heureDebut < :fdatefin OR m.heureFin > :fdatedebut AND m.heureFin < :fdatefin)");
            query.setParameter("fprenommoniteur", cours.prof.getPseudo());
            query.setParameter("fdatedebut", cours.getHeureDebut());
            query.setParameter("fdatefin", cours.getHeureFin());
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }

    @Override
    public List<Cours> getByPseudoNext(String pseudo) throws Exception {
        List<Cours> listCours = new ArrayList();
        Date dateNow = new Date();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.eleve.pseudo = :fpseudo AND m.heureDebut > :fdatenow AND m.valide = 1 ORDER BY m.heureDebut");
            query.setParameter("fpseudo", pseudo);
            query.setParameter("fdatenow", dateNow);
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }
    
    @Override
    public List<Cours> getByPseudoNextAttente(String pseudo) throws Exception {
        List<Cours> listCours = new ArrayList();
        Date dateNow = new Date();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.eleve.pseudo = :fpseudo AND m.heureDebut > :fdatenow AND m.valide = 0 ORDER BY m.heureDebut");
            query.setParameter("fpseudo", pseudo);
            query.setParameter("fdatenow", dateNow);
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }

    @Override
    public List<Cours> getByCoursValide() throws Exception {
        List<Cours> listCours = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.valide = 1");
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }

    @Override
    public List<Cours> getByCoursValideByProf(User prof) throws Exception {
        List<Cours> listCours = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.valide = 1 AND m.prof = :prof");
            query.setParameter("prof", prof);
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }

    @Override
    public List<Cours> getByDateVoiture(Date debut, Date fin) throws Exception {
        List<Cours> listCours = new ArrayList();
        try {
            this.open();
            Query query = em.createQuery("SELECT m FROM Cours m WHERE m.valide = 1 AND (m.heureDebut >= :fdatedebut AND m.heureDebut < :fdatefin OR m.heureFin > :fdatedebut AND m.heureFin < :fdatefin)");
            query.setParameter("fdatedebut", debut);
            query.setParameter("fdatefin", fin);
            listCours = query.getResultList();
        } finally {
            this.close();
        }
        return listCours;
    }
    
}
