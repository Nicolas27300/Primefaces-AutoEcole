package metier;

import java.util.Date;
import java.util.List;
import lml.persistence.CrudService;

public interface CoursService extends CrudService<Cours> {
    
    List<Cours> getByNonValide(User prof) throws Exception;
    List<Cours> getByDateMoniteur(Cours cours) throws Exception;
    List<Cours> getByPseudoNext(String pseudo) throws Exception;
    List<Cours> getByPseudoNextAttente(String pseudo) throws Exception;
    List<Cours> getByCoursValide() throws Exception;
    List<Cours> getByCoursValideByProf(User prof) throws Exception;
    List<Cours> getByDateVoiture(Date debut, Date fin) throws Exception;
}
