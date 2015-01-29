package impl;

import ids.Items;
import ids.Systems;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import quicklook.EveCentralApi;
import quicklook.Order;

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
