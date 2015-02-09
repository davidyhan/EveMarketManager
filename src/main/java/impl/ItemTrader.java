package impl;

import trading.ItemTrading;

public class ItemTrader {

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";
        String dbPath = "C:\\Users\\David\\Dropbox\\Eve\\items.txt";

        ItemTrading itemTrading = new ItemTrading();

        // itemTrading.updateSingleItemPrice(filePath, 91);
        itemTrading.updateItemSheet(filePath, dbPath);

        System.out.println("Fin");
    }
}
