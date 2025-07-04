package com.qa.api.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelUtil {

    private static String TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/APITestData.xlsx";
    private static Workbook workbook;
    private static Sheet sheet;

    public static Object[][] readData (String sheetName) {
        Object data[][] = null;

        try {
            FileInputStream ip = new FileInputStream(TEST_DATA_SHEET_PATH);

            workbook = WorkbookFactory.create(ip);
            sheet = workbook.getSheet(sheetName);

            data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
                    data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }
}
