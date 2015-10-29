package trading;

import ids.Systems;
import impl.EveApiImpl;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import EveApi.CharOrder;
import EveApi.EveApi;
import exceptions.ExcelException;
import exceptions.ItemNotFoundException;

public class ItemTrading {
    private EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);
    private ArrayList<Integer> systems = new ArrayList<Integer>();
    private EveApiImpl api = new EveApiImpl();

    private final static String NarwhalApi = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4413855&vCode=mXeJY5fSA9YKp16zq0kgXTeYvjCwaAHoVKhDOjLK8x3iJ1su2Q9zENaSWY7vmEnZ";

    public ItemTrading() {
        systems.add(Systems.UH);
        systems.add(Systems.JITA);
    }

    public void updateItemSheet(String file, String dbPath) throws Exception {
        // Make sure all the items have their id's bound
        bindItemIds(file, dbPath);

        List<CharOrder> orders = quickLook.unmarshal(quickLook.queryEveCentralUrl(NarwhalApi), EveApi.class).getResult().getRowset().getListOrders();

        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        HashMap<String, Integer> itemMap = parseItemMap(sheet);

//        for (String key : itemMap.keySet()) {
//            updateItemPriceForAllSystems(key, itemMap.get(key), sheet);
//        }

        //calculateProfitMargins(sheet, 0);
        //colorProfitMargins(sheet, new Coordinate(3, 2), wb);
        api.updateCharacterOrderAmount(sheet, orders);

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

    }

    public void updateItemSheetSingleItem(String file, String dbPath, String itemName, Integer itemId) throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        updateItemPriceForAllSystems(itemName, itemId, sheet);

        calculateProfitMargins(sheet, 0);
        colorProfitMargins(sheet, new Coordinate(3, 1), wb);

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
                Cell writeSpot = sheet.getRow(c.getX()).getCell(i + 3);

                if (writeSpot == null) {
                    writeSpot = sheet.getRow(c.getX()).createCell(i + 3);
                    writeSpot.setCellType(Cell.CELL_TYPE_NUMERIC);
                }

                writeSpot.setCellValue(lowestPrice);
            } else {
                Cell nullify = sheet.getRow(c.getX()).getCell(c.getY() + 3);
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
                if (c.getCellType() == Cell.CELL_TYPE_STRING) {
                    String name = c.getStringCellValue();

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
            Cell nameCell = r.getCell(1);
            Cell idCell = r.getCell(0);

            // Makes sure the cell is not null + is type string + does not
            // contains *
            if (nameCell != null && nameCell.getCellType() == Cell.CELL_TYPE_STRING && !nameCell.getStringCellValue().contains("*")) {
                String itemName = nameCell.getStringCellValue();
                Integer itemId = (int) idCell.getNumericCellValue();

                items.put(itemName, itemId);
            }
        }

        return items;
    }

    public void bindItemIds(String itemTradePath, String itemDBPath) throws Exception {

        FileInputStream fsIP = new FileInputStream(new File(itemTradePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        // Goes through the first column of each row (only item names + id's
        // should be on this row)
        for (Row r : sheet) {
            Cell idCell = r.getCell(0);
            Cell nameCell = r.getCell(1);

            // Filters out titles + item who already have id's binded
            if (nameCell != null && nameCell.getCellType() == Cell.CELL_TYPE_STRING && !nameCell.getStringCellValue().contains("*")) {
                String itemName = nameCell.getStringCellValue();
                if (idCell == null || idCell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                    Integer itemId = getItemIdFromDB(itemDBPath, itemName);

                    if (itemId == -1) {
                        wb.close();
                        throw new ExcelException(itemName + " does not exist in the item database");
                    }

                    if (idCell == null) {
                        idCell = r.createCell(0);
                        idCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    }

                    System.out.println("Item :" + itemName + " binded with id: " + itemId);

                    idCell.setCellValue(itemId);
                }
            }
        }

        FileOutputStream output_file = new FileOutputStream(new File(itemTradePath));
        wb.write(output_file); // write changes
        output_file.close();
        wb.close();
    }

    private Integer getItemIdFromDB(String itemDBPath, String itemName) throws Exception {
        Integer id = -1;

        FileInputStream fsIP = new FileInputStream(new File(itemDBPath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        for (Row r : sheet) {
            if (r.getCell(2) != null && r.getCell(2).getCellType() == Cell.CELL_TYPE_STRING && r.getCell(2).getStringCellValue().equals(itemName)) {
                wb.close();
                return (int) r.getCell(0).getNumericCellValue();
            }
        }

        wb.close();

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
        Cell c = r.getCell(1);
        if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*")) {
            if (r.getCell(3) != null && r.getCell(4) != null) {
                Double stagingPrice = r.getCell(3).getNumericCellValue();
                Double amarrPrice = r.getCell(4).getNumericCellValue();

                Double profitPercentage = ((stagingPrice - amarrPrice) / amarrPrice) * 100;

                Cell profitCell = r.getCell(2);

                if (profitCell == null) {
                    profitCell = r.createCell(2);
                }

                profitCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                profitCell.setCellValue(profitPercentage);
            } else {
                Cell margin = r.getCell(2);
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
                    } else if (value < 0 && value > -20) {
                        cell.setCellStyle(orange);
                    } else {
                        cell.setCellStyle(clear);
                    }
                }
            }
        }
    }
}
