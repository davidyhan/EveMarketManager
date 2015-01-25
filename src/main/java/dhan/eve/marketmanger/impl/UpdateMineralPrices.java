package dhan.eve.marketmanger.impl;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import dhan.eve.marketmanager.trading.Minerals;
import dhan.eve.marketmanger.ids.Systems;

public class UpdateMineralPrices {
    
    public static void main(String[] args) throws IOException, JAXBException{
        Minerals min = new Minerals();
        
        String file = "C:\\Users\\David\\Eve\\ShipManufacturing.xlsx";
        
        min.updateMineralPrices(file, Systems.AMARR);
        
        System.out.println("Fin");
    }
}
