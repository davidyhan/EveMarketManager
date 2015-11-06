package impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import EveApi.CharOrder;
import EveApi.EveApi;

public class Debugger {
    private final static String NarwhalApi = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4413855&vCode=mXeJY5fSA9YKp16zq0kgXTeYvjCwaAHoVKhDOjLK8x3iJ1su2Q9zENaSWY7vmEnZ";
    private static EveCentral eve = new EveCentral("");

    static String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";

    // Testing new code
    public static void main(String[] args) throws Exception {

        debugCharacterOrders();
        // printCharacterOrders();
        // setCellValue();
    }

    public static void debugCharacterOrders() throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(filePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        EveApiImpl charOrders = new EveApiImpl();
        List<CharOrder> orders = eve.unmarshal(eve.queryEveCentralUrl(NarwhalApi), EveApi.class).getResult().getRowset().getListOrders();
        // charOrders.updateCharacterOrderAmount(sheet, orders);

        charOrders.listMissingOrders(sheet, orders);

        FileOutputStream output_file = new FileOutputStream(new File(filePath));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

        System.out.println("FIN");

    }

    public static void printCharacterOrders() throws Exception {
        EveCentral quickLook = new EveCentral(EveCentral.quickLookBase);

        List<CharOrder> orders = quickLook.unmarshal(quickLook.queryEveCentralUrl(NarwhalApi), EveApi.class).getResult().getRowset().getListOrders();

        for (CharOrder o : orders) {
            System.out.println(o.toString());
        }
    }

    public static void setCellValue() throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(filePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        sheet.getRow(58).createCell(5);
        sheet.getRow(58).getCell(5).setCellValue("88/88");

        FileOutputStream output_file = new FileOutputStream(new File(filePath));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();
    }

    public static void printLineValue() throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(filePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        System.out.println(sheet.getRow(58).getCell(0).getStringCellValue() + " - " + sheet.getRow(58).getCell(5).getStringCellValue());

        wb.close();
    }

    public static String queryURL(String fullURL) throws IOException {
        URL obj = new URL(fullURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
