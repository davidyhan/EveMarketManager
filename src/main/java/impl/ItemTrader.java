package impl;

import trading.ItemTrading;

public class ItemTrader {

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";

        ItemTrading itemTrading = new ItemTrading();
        itemTrading.updateItemSheet(filePath);
    }
}
