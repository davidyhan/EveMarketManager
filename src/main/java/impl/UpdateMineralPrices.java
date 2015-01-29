package impl;

import ids.Systems;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import trading.Minerals;

public class UpdateMineralPrices {
    
    public static void main(String[] args) throws IOException, JAXBException{
        Minerals min = new Minerals();
        
        String file = "C:\\Users\\David\\Dropbox\\Eve\\ShipManufacturing.xlsx";
        
        min.updateMineralPrices(file, Systems.AMARR);
        
        System.out.println("Fin");
    }
}
