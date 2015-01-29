package trading;

import ids.Systems;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quicklook.EveCentralApi;
import quicklook.Order;
import quicklook.SellOrders;
import exceptions.ItemNotFoundException;

public class ItemTrading {
    EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);
    ArrayList<Integer> systems = new ArrayList<Integer>();

    public ItemTrading() {
        systems.add(Systems.GE);
        systems.add(Systems.AMARR);
    }

    public void updateItemSheet(HashMap<String, Integer> itemMap, String file) throws IOException, ItemNotFoundException, JAXBException {
        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        for (String key : itemMap.keySet()) {
            updateItemPriceForAllSystems(key, itemMap.get(key), sheet);
        }

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

    }

    // Updates the item from for each System on the item trading excel sheet
    public void updateItemPriceForAllSystems(String itemName, Integer itemId, XSSFSheet sheet) throws ItemNotFoundException, JAXBException, IOException {
        Coordinate c = getItemCoordinate(itemName, sheet);

        if (c == null) {
            throw new ItemNotFoundException(itemName + " is not defined in item trader excel doc");
        }

        for (int i = 0; i < systems.size(); i++) {
            int system = systems.get(i);

            EveCentralApi item = quickLook.unmarshal(quickLook.queryItemBySystem(itemId, system), EveCentralApi.class);
            SellOrders sellOrders = item.getQuick().getSellOrder();

            Double lowestPrice = getLowestSellPrice(sellOrders);
            System.out.println("Lowest Price: "+lowestPrice);

            // TODO Possibly use method for this
            Cell writeSpot = sheet.getRow(c.getX()).getCell(i + 1);
            
            if(writeSpot == null){
                writeSpot = sheet.getRow(c.getX()).createCell(i+1);
                writeSpot.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            
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
                if (c.getCellType()== Cell.CELL_TYPE_STRING && c.getStringCellValue().equals(itemName)) {
                    return new Coordinate(c.getRowIndex(), c.getColumnIndex());
                }
            }
        }
        return null;
    }
}
