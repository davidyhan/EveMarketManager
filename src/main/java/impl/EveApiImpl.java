package impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import EveApi.CharOrder;
import EveApi.EveApi;
import exceptions.ItemNotFoundException;

public class EveApiImpl {
    private final static String NarwhalApi = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4411599&vCode=M92INSxszKofWhN02pVpla8QO1yl76It197OSMeZ8BTrcy33QZ3EjZ4QUkBoKsAt";

    private EveCentral eve = new EveCentral(EveCentral.quickLookBase);

    // Updates the item trade sheet with Character order amounts provided by Eve online api keys
    public void updateCharacterOrderAmount(XSSFSheet sheet) throws Exception {
        List<CharOrder> orders = eve.unmarshal(eve.queryEveCentralUrl(NarwhalApi), EveApi.class).getResult().getRowset().getListOrders();
        clearUnitsColumn(sheet);

        for (CharOrder order : orders) {
            Integer itemId = order.getTypeId();
            String orderRatio = "" + order.getVolRemaining() + "/" + order.getVolEntered();

            Row r = getRowFromId(sheet, itemId);

            if (r != null) {
                if (order.getOrderState() == 0) {
                    Cell c = r.getCell(5);
                    if (c == null) {
                        c = r.createCell(5);
                    }
                    c.setCellValue(orderRatio);
                } else if (order.getOrderState() == 2 && r.getCell(5) != null) {
                    r.removeCell(r.getCell(5));
                }
            } else {
                if (!(order.getOrderState() == 2)) {
                    throw new ItemNotFoundException("Item with id: " + itemId + " does not exist in spreadsheet");
                }
            }

        }
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
