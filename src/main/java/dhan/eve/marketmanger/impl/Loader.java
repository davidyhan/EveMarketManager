package dhan.eve.marketmanger.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import dhan.eve.marketmanager.evecentral.quicklook.EveCentralApi;
import dhan.eve.marketmanager.evecentral.quicklook.Order;
import dhan.eve.marketmanger.ids.Items;
import dhan.eve.marketmanger.ids.Systems;

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
