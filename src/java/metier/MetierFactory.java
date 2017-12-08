package metier;

public class MetierFactory {

    private MetierFactory() {
    }

    private static UserService userSrv = null;
    private static VoitureService voitureSrv = null;
    private static CoursService coursSrv = null;

    public synchronized static UserService getUserService() throws Exception {
        if (userSrv == null) {
            setSession();
        }

        return MetierFactory.userSrv;
    }
    
    public synchronized static VoitureService getVoitureService() throws Exception {
        if (voitureSrv == null) {
            setSession();
        }

        return MetierFactory.voitureSrv;
    }
    
    public synchronized static CoursService getCoursService() throws Exception {
        if (coursSrv == null) {
            setSession();
        }

        return MetierFactory.coursSrv;
    }

    public static void setSession() {
        userSrv = new UserServiceImpl("AutoEcolePU");
        voitureSrv = new VoitureServiceImpl("AutoEcolePU");
        coursSrv = new CoursServiceImpl("AutoEcolePU");
    }
}
