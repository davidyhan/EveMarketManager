package ids;

import java.util.HashMap;

public class Systems {
    HashMap<String, Integer> systems = new HashMap<String, Integer>();

    /* SYSTEMS */
    // Trade Hubs
    public static final int DODIXIE = 30002659;
    public static final int AMARR = 30002187;
    public static final int JITA = 30000142;
    public static final int RENS = 30002510;

    // Catch
    public static final int GE = 30001198;
    public static final int HED = 30001161;
    
    //Wicked Creek
    public static final int UH = 30000575;

    /* REGIONS */
    public static final int CATCH = 10000014;
    public static final int DOMAIN = 10000043;

    public Systems() {
        // Trade Hubs
        systems.put("DODIXIE", DODIXIE);
        systems.put("AMARR", AMARR);
        systems.put("JITA", JITA);
        systems.put("RENS", RENS);

        // Catch
        systems.put("GE", GE);

        // Regions
        systems.put("CATCH", CATCH);
        systems.put("DOMAIN", DOMAIN);
    }
}
