package dhan.eve.marketmanager.trading;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dhan.eve.marketmanager.evecentral.quicklook.EveCentralApi;
import dhan.eve.marketmanager.evecentral.quicklook.Order;
import dhan.eve.marketmanager.evecentral.quicklook.SellOrders;
import dhan.eve.marketmanager.exceptions.ItemNotFoundException;
import dhan.eve.marketmanger.ids.Systems;
import dhan.eve.marketmanger.impl.EveCentral;

public class ItemTrading {
    EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);
    ArrayList<Integer> systems = new ArrayList<Integer>();

    public ItemTrading() {
        systems.add(Systems.GE);
        systems.add(Systems.AMARR);
    }

    public void updateItemPriceForAllSystems(String itemName, Integer itemId, XSSFSheet sheet) throws ItemNotFoundException, JAXBException, IOException {
        Coordinate c = getItemCoordinate(itemName, sheet);

        if (c == null) {
            throw new ItemNotFoundException();
        }

        for (int i = 0; i < systems.size(); i++) {
            int system = systems.get(i);

            EveCentralApi item = quickLook.unmarshal(quickLook.queryItemBySystem(itemId, system), EveCentralApi.class);
            SellOrders sellOrders = item.getQuick().getSellOrder();
            
            Double lowestPrice = getLowestSellPrice(sellOrders);
            
            Cell writeSpot = sheet.getRow(c.getX()).getCell(i+1);
            
            writeSpot.setCellValue(lowestPrice);
        }
    }

    public void updateItemPriceForSystem(int item, Double price, Coordinate c, XSSFSheet sheet) throws JAXBException, IOException {
        Cell cell = sheet.getRow(c.getX()).getCell(c.getY());
        cell.setCellValue(price);
    }

    public Double getLowestSellPrice(SellOrders orders) {
        Collections.sort(orders.getListOrders());

        return orders.getListOrders().get(0).getPrice();
    }

    public Integer getTotalVolume(SellOrders orders) {
        Integer totalOrders = 0;

        for (Order o : orders.getListOrders()) {
            totalOrders += o.getVolumeRemaining();
        }

        return totalOrders;
    }

    public Coordinate getItemCoordinate(String itemName, XSSFSheet sheet) {
        for (Row r : sheet) {
            for (Cell c : r) {
                if (c.getStringCellValue().equals(itemName)) {
                    return new Coordinate(c.getRowIndex(), c.getColumnIndex());
                }
            }
        }
        return null;
    }
}
