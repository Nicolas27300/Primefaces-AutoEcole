package metier;

import java.util.List;
import lml.persistence.CrudService;


public interface UserService extends CrudService<User> {
    User getByPseudo(String pseudo) throws Exception;
    List<User> getByMoniteur() throws Exception;
    List<User> getByEmail(String email) throws Exception;
    List<User> getByPseudoUtilise(String email) throws Exception;
}
