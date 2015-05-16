package trading;

import ids.Systems;
import impl.EveApiImpl;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quicklook.EveCentralApi;
import quicklook.SellOrders;
import exceptions.ExcelException;
import exceptions.ItemNotFoundException;

public class ItemTrading {
    private EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);
    private ArrayList<Integer> systems = new ArrayList<Integer>();
    private EveApiImpl api = new EveApiImpl();

    public ItemTrading() {
        systems.add(Systems.UH);
        systems.add(Systems.AMARR);
    }

    public void updateItemSheet(String file, String dbPath) throws Exception {
        // Make sure all the items have their id's bound
        bindItemIds(file, dbPath);

        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        HashMap<String, Integer> itemMap = parseItemMap(sheet);

        for (String key : itemMap.keySet()) {
            updateItemPriceForAllSystems(key, itemMap.get(key), sheet);
        }

        calculateProfitMargins(sheet, 0);
        colorProfitMargins(sheet, new Coordinate(3, 1), wb);
        api.updateCharacterOrderAmount(sheet);

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

    }

    // Updates the item from for each System on the item trading excel sheet
    private void updateItemPriceForAllSystems(String itemName, Integer itemId, XSSFSheet sheet) throws Exception {
        System.out.println("Item Name: " + itemName + ", Item Id: " + itemId);

        Coordinate c = getItemCoordinate(itemName, sheet);

        if (c == null) {
            throw new ItemNotFoundException(itemName + " is not defined in item trader excel doc");
        }

        for (int i = 0; i < systems.size(); i++) {
            int system = systems.get(i);

            EveCentralApi item = quickLook.unmarshal(quickLook.queryItemBySystem(itemId, system), EveCentralApi.class);
            SellOrders sellOrders = item.getQuick().getSellOrder();

            // Checks to make sure there exist sell orders at the target system
            if (sellOrders.getListOrders() != null) {

                Double lowestPrice = getLowestSellPrice(sellOrders);

                // TODO Possibly use method for this
                Cell writeSpot = sheet.getRow(c.getX()).getCell(i + 2);

                if (writeSpot == null) {
                    writeSpot = sheet.getRow(c.getX()).createCell(i + 2);
                    writeSpot.setCellType(Cell.CELL_TYPE_NUMERIC);
                }

                writeSpot.setCellValue(lowestPrice);
            } else {
                Cell nullify = sheet.getRow(c.getX()).getCell(c.getY() + 2);
                if (nullify != null) {
                    sheet.getRow(c.getX()).removeCell(nullify);
                }
            }
        }
    }

    public Double getLowestSellPrice(SellOrders orders) {
        Collections.sort(orders.getListOrders());

        return orders.getListOrders().get(0).getPrice();
    }

    private Coordinate getItemCoordinate(String itemName, XSSFSheet sheet) {
        for (Row r : sheet) {
            for (Cell c : r) {
                if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().contains(" - ")) {
                    String val = c.getStringCellValue();
                    String name = val.substring(0, val.indexOf(" - "));

                    if (name.equals(itemName)) {
                        return new Coordinate(c.getRowIndex(), c.getColumnIndex());
                    }
                }
            }
        }
        return null;
    }

    // Return a hash map of the items in the item trader file with their
    // corresponding item id's
    private HashMap<String, Integer> parseItemMap(XSSFSheet sheet) throws ExcelException {
        HashMap<String, Integer> items = new HashMap<String, Integer>();

        for (Row r : sheet) {
            Cell c = r.getCell(0);

            // Makes sure the cell is not null + is type string + does not
            // contains *
            if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*")) {
                String combined = c.getStringCellValue();
                int delimiter = combined.indexOf(" - ");

                if (!combined.contains(" - ")) {
                    throw new ExcelException(combined + " does not contain a - delimiter or is incorrectly formatted");
                }

                String itemName = combined.substring(0, delimiter).trim();
                Integer itemId = Integer.parseInt(combined.substring(delimiter + 3).trim());

                items.put(itemName, itemId);
            }
        }

        return items;
    }

    public void bindItemIds(String itemTradePath, String itemDBPath) throws Exception {
        List<String> itemDB = Files.readAllLines(new File(itemDBPath).toPath(), Charset.defaultCharset());

        FileInputStream fsIP = new FileInputStream(new File(itemTradePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        // Goes through the first column of each row (only item names + id's
        // should be on this row)
        for (Row r : sheet) {
            Cell c = r.getCell(0);

            // Filters out titles + item who already have id's binded
            if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING) {
                String itemName = c.getStringCellValue();
                if (!itemName.contains("*") && !itemName.contains(" - ")) {
                    Integer itemId = getItemIdFromDB(itemDB, itemName);

                    if (itemId == -1) {
                        throw new ExcelException(itemName + " does not exist in the item database");
                    }

                    String newValue = itemName + " - " + itemId;

                    System.out.println(newValue);

                    c.setCellValue(newValue);
                }
            }
        }

        FileOutputStream output_file = new FileOutputStream(new File(itemTradePath));
        wb.write(output_file); // write changes
        output_file.close();
        wb.close();
    }

    private Integer getItemIdFromDB(List<String> db, String itemName) {
        Integer id = -1;

        for (String s : db) {
            if (s.contains("    ")) {
                String formatted = s.substring(s.indexOf("    ")).trim();
                if (itemName.equals(formatted)) {
                    id = Integer.parseInt(s.substring(0, s.indexOf("    ")).trim());
                    return id;
                }
            }
        }

        return id;
    }

    // Calculates the profit margins for items between Staging system (Currently
    // U-H) and Amarr
    public void calculateProfitMargins(XSSFSheet sheet, int start) throws IOException {
        for (Row r : sheet) {
            if (r.getRowNum() >= start) {
                calculateProfitMarginSingle(sheet, r);
            }
        }
    }

    private void calculateProfitMarginSingle(XSSFSheet sheet, Row r) {
        // gets the first cell to check if it's a item row
        Cell c = r.getCell(0);
        if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*") && c.getStringCellValue().contains(" - ")) {
            if (r.getCell(2) != null && r.getCell(3) != null) {
                Double stagingPrice = r.getCell(2).getNumericCellValue();
                Double amarrPrice = r.getCell(3).getNumericCellValue();

                Double profitPercentage = ((stagingPrice - amarrPrice) / amarrPrice) * 100;

                Cell profitCell = r.getCell(1);

                if (profitCell == null) {
                    profitCell = r.createCell(1);
                }

                profitCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                profitCell.setCellValue(profitPercentage);
            } else {
                Cell margin = r.getCell(1);
                if (margin != null) {
                    r.removeCell(margin);
                }
            }
        }
    }

    // Fills in color for profit margins
    private void colorProfitMargins(XSSFSheet sheet, Coordinate c, XSSFWorkbook wb) {
        // sets up the colors
        XSSFCellStyle green = wb.createCellStyle();
        green.setFillForegroundColor(new XSSFColor(new java.awt.Color(149, 255, 149)));
        green.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFCellStyle red = wb.createCellStyle();
        red.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 135, 135)));
        red.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFCellStyle orange = wb.createCellStyle();
        orange.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 191, 135)));
        orange.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFCellStyle clear = wb.createCellStyle();
        clear.setFillPattern(CellStyle.NO_FILL);

        // iterates through the excel starting at the starting coordinate
        for (Row r : sheet) {
            if (r.getRowNum() >= c.getX()) {
                Cell cell = r.getCell(c.getY());

                // checks to make sure the cell coming back isn't null
                if (cell != null) {
                    // System.out.println(cell.getRowIndex() + ", " + cell.getColumnIndex());
                    Double value = cell.getNumericCellValue();
                    if (value > 40) {
                        cell.setCellStyle(green);
                    } else if (value < -20) {
                        cell.setCellStyle(red);
                    } else {
                        cell.setCellStyle(clear);
                    }
                }
            }
        }
    }
}
