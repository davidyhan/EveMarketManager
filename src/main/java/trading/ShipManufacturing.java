package trading;

import ids.Systems;
import impl.EveCentral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quicklook.EveCentralApi;
import quicklook.SellOrders;

public class ShipManufacturing {
    EveCentral quick = new EveCentral(EveCentral.quickLookBase);
    ItemTrading items = new ItemTrading();

    public void updateShipPrices(String file) throws Exception {

        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        Coordinate start = new Coordinate(14, 0);

        for (Row r : sheet) {
            if (r.getRowNum() > start.getX()) {
                Cell c = r.getCell(0);

                if (c != null && c.getCellType() == Cell.CELL_TYPE_STRING && !c.getStringCellValue().contains("*")) {
                    String cellVal = c.getStringCellValue();

                    Integer itemNum = Integer.parseInt(cellVal.substring(cellVal.indexOf(" - ") + 3));
                    
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
        EveCentralApi query = quick.unmarshal(quick.queryItemBySystem(itemNum, Systems.GE), EveCentralApi.class);
        
        SellOrders sellOrders = query.getQuick().getSellOrder();
        
        Double lowest = items.getLowestSellPrice(sellOrders);
        
        if(lowest != null){
            Cell c = sheet.getRow(loc.getX()).getCell(loc.getY());
            
            if(c == null){
                c = sheet.getRow(loc.getX()).createCell(loc.getY());
                c.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            
            c.setCellValue(lowest);
        }
    }
}
