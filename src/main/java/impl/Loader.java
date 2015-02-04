package impl;


@SuppressWarnings("restriction")
public class Loader {

    static EveCentral quickLook;
    static EveCentral marketStat;

    public static void main(String[] args) throws Exception {

        quickLook = new EveCentral(EveCentral.quickLookBase);
        marketStat = new EveCentral(EveCentral.marketStatBase);
        
        String system = "AMARR";
        
    }
}
