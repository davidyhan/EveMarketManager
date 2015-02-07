package impl;

import quicklook.EveCentralApi;
import ids.Systems;

public class Loader {

    static EveCentral quickLook;
    static EveCentral marketStat;

    public static void main(String[] args) throws Exception {

        quickLook = new EveCentral(EveCentral.quickLookBase);
        marketStat = new EveCentral(EveCentral.marketStatBase);

        EveCentralApi query = quickLook.unmarshal(quickLook.queryItemBySystem(3025, Systems.AMARR), EveCentralApi.class);
        System.out.println(quickLook.queryItemBySystem(2985, Systems.AMARR));
        System.out.println(query.getQuick());

        System.out.println("Fin");

    }
}
