package impl;

import trading.ItemTrading;

public class ItemTrader {

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";
        String dbPath = "C:\\Users\\David\\Dropbox\\Eve\\items.txt";

        ItemTrading itemTrading = new ItemTrading();

        itemTrading.updateItemSheet(filePath, dbPath);
        // itemTrading.updateSingleItemPrice(filePath, 102);

        // itemTrading.test(filePath, new Coordinate(102,3));

        System.out.println("Fin");
    }
}
