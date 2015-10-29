package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SplitTypeId {

    static String filePath = "C:\\Users\\David\\Dropbox\\Eve\\itemTrader.xlsx";

    public static void main(String[] args) throws Exception {
        FileInputStream fsIP = new FileInputStream(new File(filePath));
        XSSFWorkbook wb = new XSSFWorkbook(fsIP);
        XSSFSheet sheet = wb.getSheetAt(0);
        fsIP.close();

        for (Row r : sheet) {
            Cell name = r.getCell(1);
            if (name != null && !name.getStringCellValue().startsWith("*") && name.getStringCellValue().length() > 2) {
                String value = name.getStringCellValue();

                value = (value.substring(0, value.indexOf(" - "))).trim();

                name.setCellValue(value);

            }
        }

        FileOutputStream output_file = new FileOutputStream(new File(filePath));

        wb.write(output_file); // write changes

        output_file.close();

        wb.close();

        System.out.println("Fin");
    }
}
