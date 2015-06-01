package impl;

import trading.ItemTrading;

public class ItemTrader {

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";
        String dbPath = "C:\\Users\\David\\Dropbox\\Eve\\items.txt";

        ItemTrading itemTrading = new ItemTrading();

        itemTrading.updateItemSheet(filePath, dbPath);

        // itemTrading.updateItemSheetSingleItem(filePath, dbPath, "425mm Railgun II", 3090);

        System.out.println("Fin");
    }
}
