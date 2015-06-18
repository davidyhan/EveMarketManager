package impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import EveApi.CharOrder;

public class EveApiImpl {
    private final static String NarwhalApi = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4413855&vCode=mXeJY5fSA9YKp16zq0kgXTeYvjCwaAHoVKhDOjLK8x3iJ1su2Q9zENaSWY7vmEnZ";

    private EveCentral eve = new EveCentral(EveCentral.quickLookBase);

    // Updates the item trade sheet with Character order amounts provided by Eve online api keys
    public void updateCharacterOrderAmount(XSSFSheet sheet, List<CharOrder> orders) throws Exception {
        clearUnitsColumn(sheet);

        for (CharOrder order : orders) {
            Integer itemId = order.getTypeId();
            String orderRatio = "" + order.getVolRemaining() + "/" + order.getVolEntered();

            // System.out.println("### ItemId: " + itemId);

            Row r = getRowFromId(sheet, itemId);

            if (r != null) {
                // System.out.println("Row not null");
                if (order.getOrderState() == 0) {
                    Cell c = r.getCell(5);
                    if (c == null) {
                        // System.out.println("Created Cell");
                        c = r.createCell(5);
                        // System.out.println("Updated new Cell with ratio:" + orderRatio);
                        c.setCellValue(orderRatio);
                    } else {
                        // System.out.println("Updated Cell with ratio:" + orderRatio);
                        c.setCellValue(orderRatio);
                    }
                } else if (order.getOrderState() == 2 && r.getCell(5) != null) {
                    // System.out.println("Removed Cell");
                    r.removeCell(r.getCell(5));
                }
            } else {
                // System.out.println("Error");
                if (!(order.getOrderState() == 2)) {
                    // throw new ItemNotFoundException("Item with id: " + itemId +
                    // " does not exist in spreadsheet");
                }
            }

        }
    }

    public void listMissingOrders(XSSFSheet sheet, List<CharOrder> orders) {
        for (CharOrder order : orders) {
            if (order.getOrderState() == 0) {
                Cell ratioCell = getOrdersRatio(sheet, order.getTypeId(), order);
                // if (ratioCell == null) {
                //
                // System.out.println("Missing Character orders for item: " + order.getTypeId());
                // }
            }
        }
    }

    public Cell getOrdersRatio(XSSFSheet sheet, int itemId, CharOrder order) {
        for (Row r : sheet) {
            Cell c = r.getCell(0);
            if (c != null && c.getStringCellValue().contains(" - ")) {
                int rowItemId = Integer.parseInt((c.getStringCellValue().substring(c.getStringCellValue().indexOf(" - ") + 3)).trim());
                if (rowItemId == itemId) {
                    Cell ratio = r.getCell(5);
                    if (ratio == null) {
                        Cell newC = r.createCell(5);
                        // newC.setCellValue(order.getVolRemaining() + "/" + order.getVolEntered());
                        newC.setCellValue("Missing: " + order.getVolRemaining() + "/" + order.getVolEntered());
                    }
                }
            }
        }

        return null;
    }

    private void clearUnitsColumn(XSSFSheet sheet) {
        for (Row r : sheet) {
            Cell c = r.getCell(5);
            if (c != null) {
                r.removeCell(c);
            }
        }
    }

    private Row getRowFromId(XSSFSheet sheet, Integer id) {
        for (Row r : sheet) {
            if (r.getCell(0) != null && r.getCell(0).getStringCellValue().contains(id.toString())) {
                return r;
            }
        }

        return null;
    }
}
