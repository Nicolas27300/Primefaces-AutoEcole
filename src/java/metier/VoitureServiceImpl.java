package metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lml.persistence.jpa.AbstracCrudServiceJPA;

public class VoitureServiceImpl extends AbstracCrudServiceJPA<Voiture> implements VoitureService {
    
    VoitureServiceImpl(String PU){
        super(PU);
    }    
}
