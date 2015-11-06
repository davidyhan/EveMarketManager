package impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import EveApi.CharOrder;

public class EveApiImpl {

    // Updates the item trade sheet with Character order amounts provided by Eve online api keys
    public void updateCharacterOrderAmount(XSSFSheet sheet, List<CharOrder> orders) throws Exception {
        clearUnitsColumn(sheet);

        for (CharOrder order : orders) {
            Integer itemId = order.getTypeId();
            String orderRatio = "" + order.getVolRemaining() + "/" + order.getVolEntered();

            Row r = getRowFromId(sheet, itemId);

            if (r != null) {
                if (order.getOrderState() == 0) {
                    Cell c = r.getCell(6);

                    if (c == null) {
                        c = r.createCell(6);
                        c.setCellValue(orderRatio);
                    } else {
                        c.setCellValue(orderRatio);
                    }
                } else if (order.getOrderState() == 2 && r.getCell(6) != null) {
                    r.removeCell(r.getCell(6));
                }
            } else {
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
                if (ratioCell == null) {
                    System.out.println("Missing Character orders for item: " + order.getTypeId());
                }
            }
        }
    }

    public Cell getOrdersRatio(XSSFSheet sheet, int itemId, CharOrder order) {
        for (Row r : sheet) {
            if (r.getCell(6) != null) {
                return r.getCell(6);
            }

            Cell c = r.getCell(2);
            if (c != null && r.getCell(0) != null && r.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                int rowItemId = (int) r.getCell(0).getNumericCellValue();
                if (rowItemId == itemId) {
                    Cell ratio = r.getCell(6);
                    if (ratio == null) {
                        Cell newC = r.createCell(6);
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
            Cell c = r.getCell(6);
            if (c != null) {
                r.removeCell(c);
            }
        }
    }

    private Row getRowFromId(XSSFSheet sheet, Integer id) {
        for (Row r : sheet) {
            if (r.getCell(0) != null && r.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC && r.getCell(0).getNumericCellValue() == id) {
                return r;
            }
        }

        return null;
    }
}
