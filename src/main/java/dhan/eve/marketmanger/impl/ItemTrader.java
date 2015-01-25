package dhan.eve.marketmanger.impl;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import dhan.eve.marketmanager.exceptions.ItemNotFoundException;
import dhan.eve.marketmanager.trading.ItemTrading;
import dhan.eve.marketmanger.ids.Items;

public class ItemTrader {
    
    public static void main(String[] args) throws IOException, ItemNotFoundException, JAXBException{
        String fileLoc = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";
        Items items = new Items();
        
        HashMap<String,Integer> itemMap = items.getItemTrade();
        
        ItemTrading itemTrading = new ItemTrading();
        itemTrading.updateItemSheet(itemMap, fileLoc);
    }
}
