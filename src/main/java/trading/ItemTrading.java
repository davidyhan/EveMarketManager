package trading;

import ids.Systems;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quicklook.EveCentralApi;
import quicklook.Order;
import quicklook.SellOrders;
import exceptions.ExcelException;
import exceptions.ItemNotFoundException;

@SuppressWarnings("restriction")
public class ItemTrading {
    EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);
    ArrayList<Integer> systems = new ArrayList<Integer>();

    public ItemTrading() {
        systems.add(Systems.GE);
        systems.add(Systems.AMARR);
    }

    public void updateItemSheet(String file) throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        HashMap<String, Integer> itemMap = parseItemMap(sheet);

        for (String key : itemMap.keySet()) {
            updateItemPriceForAllSystems(key, itemMap.get(key), sheet);
        }

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

    }

    // Updates the item from for each System on the item trading excel sheet
    public void updateItemPriceForAllSystems(String itemName, Integer itemId, XSSFSheet sheet) throws Exception{
        System.out.println("Item Name: " + itemName + ", Item Id: " + itemId);

        Coordinate c = getItemCoordinate(itemName, sheet);

        if (c == null) {
            throw new ItemNotFoundException(itemName + " is not defined in item trader excel doc");
        }

        for (int i = 0; i < systems.size(); i++) {
            int system = systems.get(i);

            EveCentralApi item = quickLook.unmarshal(quickLook.queryItemBySystem(itemId, system), EveCentralApi.class);
            SellOrders sellOrders = item.getQuick().getSellOrder();

            Double lowestPrice = getLowestSellPrice(sellOrders);
            System.out.println("Lowest Price: " + lowestPrice);

            // TODO Possibly use method for this
            Cell writeSpot = sheet.getRow(c.getX()).getCell(i + 1);

            if (writeSpot == null) {
                writeSpot = sheet.getRow(c.getX()).createCell(i + 1);
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
                if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().equals(itemName)) {
                    return new Coordinate(c.getRowIndex(), c.getColumnIndex());
                }
            }
        }
        return null;
    }

    // Return a hash map of the items in the item trader file with their corresponding item id's
    public HashMap<String, Integer> parseItemMap(XSSFSheet sheet) throws ExcelException {
        HashMap<String, Integer> items = new HashMap<String, Integer>();

        for (Row r : sheet) {
            Cell c = r.getCell(0);

            // Makes sure the cell is not null + is type string + does not contains *
            if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*")) {
                String combined = c.getStringCellValue();
                int delimiter = combined.indexOf("-");

                if (!combined.contains(" - ")) {
                    throw new ExcelException(combined + " does not contain a - delimiter or is incorrectly formatted");
                }

                String itemName = combined.substring(0, delimiter).trim();
                Integer itemId = Integer.parseInt(combined.substring(delimiter + 1).trim());

                items.put(itemName, itemId);
            }
        }

        return items;
    }
    
    public XSSFSheet parseExcel(String filePath) throws IOException{
        FileInputStream fsIP = new FileInputStream(new File(filePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();
        wb.close();
        
        return sheet;
    }
}
