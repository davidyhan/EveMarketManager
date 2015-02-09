package impl;

import trading.ShipManufacturing;

public class Loader {

    public static void main(String[] args) throws Exception {
        String file = "C:\\Users\\David\\Dropbox\\Eve\\ShipManufacturing.xlsx";

        ShipManufacturing ship = new ShipManufacturing();

        ship.test(file);
    }
}
