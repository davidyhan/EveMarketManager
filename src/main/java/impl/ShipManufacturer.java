package impl;

import ids.Systems;
import trading.ItemTrading;
import trading.Minerals;
import trading.ShipManufacturing;

public class ShipManufacturer {

    public static void main(String[] args) throws Exception {
        Minerals minerals = new Minerals();
        ShipManufacturing ships = new ShipManufacturing();
        ItemTrading items = new ItemTrading();

        String file = "C:\\Users\\David\\Dropbox\\Eve\\ShipManufacturing.xlsx";
        String dbPath = "C:\\Users\\David\\Dropbox\\Eve\\items.txt";

        minerals.updateMineralPrices(file, Systems.AMARR);
        items.bindItemIds(file, dbPath);
        ships.updateShipPrices(file);

        System.out.println("Fin");
    }
}
