package trading;

import ids.Items;
import ids.Systems;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quicklook.EveCentralApi;
import quicklook.SellOrders;

public class ShipManufacturing {
    EveCentral quick = new EveCentral(EveCentral.quickLookBase);
    ItemTrading items = new ItemTrading();
    Coordinate minerals = new Coordinate(0, 0);

    public void updateShipPrices(String file) throws Exception {

        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        fsIP.close();

        Coordinate start = new Coordinate(14, 0);

        for (Row r : sheet) {
            if (r.getRowNum() > start.getX()) {
                Cell c = r.getCell(0);

                if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*")) {
                    String cellVal = c.getStringCellValue();
                    System.out.println(cellVal);

                    String itemName = cellVal.substring(0, cellVal.indexOf(" - "));
                    Integer itemNum = Integer.parseInt(cellVal.substring(cellVal.indexOf(" - ") + 3));

                    updateManufactureCosts(sheet, itemName, new Coordinate(r.getRowNum(), 3), evaluator);
                    updateItemForSystem(itemNum, sheet, new Coordinate(r.getRowNum(), 2));
                }
            }
        }

        items.calculateProfitMargins(sheet, 14);

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();
    }

    public void updateItemForSystem(int itemNum, XSSFSheet sheet, Coordinate loc) throws Exception {
        EveCentralApi query = quick.unmarshal(quick.queryItemBySystem(itemNum, Systems.UH), EveCentralApi.class);

        SellOrders sellOrders = query.getQuick().getSellOrder();

        if (sellOrders.getListOrders() != null) {
            Double lowest = items.getLowestSellPrice(sellOrders);

            if (lowest != null) {
                Cell c = sheet.getRow(loc.getX()).getCell(loc.getY());

                if (c == null) {
                    c = sheet.getRow(loc.getX()).createCell(loc.getY());
                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                }

                c.setCellValue(lowest);
            }
        } else {
            Cell c = sheet.getRow(loc.getX()).getCell(loc.getY());
            if (c != null) {
                sheet.getRow(loc.getX()).removeCell(c);
            }
        }
    }

    public void updateManufactureCosts(XSSFSheet sheet, String ship, Coordinate c, FormulaEvaluator eval) {
        Coordinate shipLoc = findShip(sheet, ship);
        int x = shipLoc.getX();
        int y = shipLoc.getY();

        Double formula = 0.0;

        int tritanium = (int) sheet.getRow(x + 2).getCell(y + 1).getNumericCellValue();
        int pyerite = (int) sheet.getRow(x + 3).getCell(y + 1).getNumericCellValue();
        int mexallon = (int) sheet.getRow(x + 4).getCell(y + 1).getNumericCellValue();
        int isogen = (int) sheet.getRow(x + 5).getCell(y + 1).getNumericCellValue();
        int nocxium = (int) sheet.getRow(x + 6).getCell(y + 1).getNumericCellValue();
        int zydrine = (int) sheet.getRow(x + 7).getCell(y + 1).getNumericCellValue();
        int megacyte = (int) sheet.getRow(x + 8).getCell(y + 1).getNumericCellValue();

        formula += getMineralPrice(sheet, Items.TRITANIUM) * tritanium;
        formula += getMineralPrice(sheet, Items.PYERITE) * pyerite;
        formula += getMineralPrice(sheet, Items.MEXALLON) * mexallon;
        formula += getMineralPrice(sheet, Items.ISOGEN) * isogen;
        formula += getMineralPrice(sheet, Items.NOCXIUM) * nocxium;
        formula += getMineralPrice(sheet, Items.ZYDRINE) * zydrine;
        formula += getMineralPrice(sheet, Items.MEGACYTE) * megacyte;

        Cell write = sheet.getRow(c.getX()).getCell(c.getY());

        if (write == null) {
            write = sheet.getRow(c.getX()).createCell(c.getY());
        }

        write.setCellValue(formula);

        eval.evaluateFormulaCell(write);
    }

    public Coordinate findShip(XSSFSheet sheet, String ship) {
        String formattedName = ">" + ship;

        for (Row r : sheet) {
            for (Cell c : r) {
                if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().equals(formattedName)) {
                    return new Coordinate(r.getRowNum(), c.getColumnIndex());
                }
            }
        }

        return null;
    }

    public Double getMineralPrice(XSSFSheet sheet, Integer mineral) {
        Double price = null;

        switch (mineral) {
            case Items.TRITANIUM:
                price = sheet.getRow(1).getCell(1).getNumericCellValue();
                break;
            case Items.PYERITE:
                price = sheet.getRow(2).getCell(1).getNumericCellValue();
                break;
            case Items.MEXALLON:
                price = sheet.getRow(3).getCell(1).getNumericCellValue();
                break;
            case Items.ISOGEN:
                price = sheet.getRow(4).getCell(1).getNumericCellValue();
                break;
            case Items.NOCXIUM:
                price = sheet.getRow(5).getCell(1).getNumericCellValue();
                break;
            case Items.ZYDRINE:
                price = sheet.getRow(6).getCell(1).getNumericCellValue();
                break;
            case Items.MEGACYTE:
                price = sheet.getRow(7).getCell(1).getNumericCellValue();
                break;
            case Items.MORPHITE:
                price = sheet.getRow(8).getCell(1).getNumericCellValue();
                break;
        }
        return price;
    }

}
