package dhan.eve.marketmanager.trading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dhan.eve.marketmanager.evecentral.marketstat.MSSell;
import dhan.eve.marketmanager.evecentral.marketstat.MarketApi;
import dhan.eve.marketmanager.evecentral.marketstat.Type;
import dhan.eve.marketmanger.ids.Items;
import dhan.eve.marketmanger.impl.EveCentral;

@SuppressWarnings("restriction")
public class Minerals {

    EveCentral marketStat = new EveCentral(EveCentral.marketStatBase);

    public void updateMineralPrices(String file, int system) throws IOException, JAXBException {
        FileInputStream fsIP = new FileInputStream(new File(file));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        Coordinate c = findMinerals(sheet);

        int x = c.getX();
        int y = c.getY();

        Cell trit = sheet.getRow(x + 1).getCell(y + 1);
        Cell pyerite = sheet.getRow(x + 2).getCell(y + 1);
        Cell mexallon = sheet.getRow(x + 3).getCell(y + 1);
        Cell isogen = sheet.getRow(x + 4).getCell(y + 1);
        Cell nocxium = sheet.getRow(x + 5).getCell(y + 1);
        Cell zydrine = sheet.getRow(x + 6).getCell(y + 1);
        Cell megactye = sheet.getRow(x + 7).getCell(y + 1);

        MarketApi minerals = marketStat.queryMinerals(system);

        MSSell trit_sell = getMineralSellOrder(minerals, Items.TRIT);
        MSSell pyer_sell = getMineralSellOrder(minerals, Items.PYERITE);
        MSSell mex_sell = getMineralSellOrder(minerals, Items.MEXALLON);
        MSSell iso_sell = getMineralSellOrder(minerals, Items.ISOGEN);
        MSSell nocx_sell = getMineralSellOrder(minerals, Items.NOCXIUM);
        MSSell zydr_sell = getMineralSellOrder(minerals, Items.ZYDRINE);
        MSSell mgcy_sell = getMineralSellOrder(minerals, Items.MEGACYTE);

        trit.setCellValue(trit_sell.getPercentile());
        pyerite.setCellValue(pyer_sell.getPercentile());
        mexallon.setCellValue(mex_sell.getPercentile());
        isogen.setCellValue(iso_sell.getPercentile());
        nocxium.setCellValue(nocx_sell.getPercentile());
        zydrine.setCellValue(zydr_sell.getPercentile());
        megactye.setCellValue(mgcy_sell.getPercentile());

        FileOutputStream output_file = new FileOutputStream(new File(file));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();
    }

    public Coordinate findMinerals(XSSFSheet sheet) {
        for (Row r : sheet) {
            for (Cell c : r) {
                if ((c.getCellType() == Cell.CELL_TYPE_STRING) && c.getStringCellValue().equals("Minerals")) {
                    return new Coordinate(c.getRowIndex(), c.getColumnIndex());
                }
            }
        }

        return null;
    }

    public MSSell getMineralSellOrder(MarketApi api, int item) {
        for (Type t : api.getMarket().getTypes()) {
            if (t.getItemId() == item) {
                return t.getSellAverage();
            }
        }
        return null;
    }
}
