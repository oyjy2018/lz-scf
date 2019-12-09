package com.zhjs.scfcloud.util.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;


/**
 * excel工具类
 * @author: dailongting
 * @date:2019/5/30 10:48
 */
@Component
public class ExcelUtil {

    private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";

    /***
     * <pre>
     * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类)
     *   xls:HSSFWorkbook
     *   xlsx：XSSFWorkbook
     * @param filePath
     * @return
     * @throws IOException
     * </pre>
     */
    public static Workbook getWorkbook(String filePath)  {
        Workbook workbook = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (filePath.endsWith(EXTENSION_XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (filePath.endsWith(EXTENSION_XLSX)) {
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 读取excel内容
     *
     */
    public static List<Map<Integer,String>> readExcel(String filePath)  {
        List<Map<Integer,String>> resultList = new ArrayList<>();
        /**
         * 这里根据不同的excel类型
         */
        // 获得工作簿
        Workbook workbook = getWorkbook(filePath);
        // 获得工作表
        Sheet sheet = workbook.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 0; i < rows; i++) {
            Map map = new HashMap();
            // 获取第i行数据
            Row sheetRow = sheet.getRow(i);
            for (int j = 0; j < sheetRow.getLastCellNum(); j++) {
                // 获取第0格数据
                Cell cell = sheetRow.getCell(j);
                // 调用toString方法获取内容
                String val = null;
                if (cell == null) {
                    map.put(j,val);
                    continue;
                }
                //获取类型
                short format = cell.getCellStyle().getDataFormat();
                String type = cell.getCellType().toString();

                if(format == 14 || format == 31 || format == 57 || format == 58){ //日期
                    Date date = cell.getDateCellValue();
                    val = DateUtil.format(date,"yyyy-MM-dd");
                }else if(format == 22){ //时间
                    Date date = cell.getDateCellValue();
                    val = DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
                }
                else if ("NUMERIC".equals(type)) { //数字类型
                    NumberFormat nf = NumberFormat.getInstance();
                    val = nf.format(cell.getNumericCellValue());
                    if (val.indexOf(",") >= 0) {
                        val = val.replace(",", "");
                    }
                }else {//其他 更多类型暂未去处理
                    val = cell.toString();
                }
                map.put(j,val);
            }
            resultList.add(map);
        }

        return resultList;
    }

    public static void main(String[] args) {
        List<Map<Integer, String>> list = ExcelUtil.readExcel("C:\\Users\\Administrator\\Desktop\\导入授信记录模板.xlsx");
        System.out.println(list);


        // 获得工作表
//        Sheet sheet = workbook.getSheetAt(0);
//        for (int i = 1; i < 5 ; i++) {
//            for (int j = 0; j < 5 ; j++) {
//                Cell cell = sheet.getRow(i).getCell(j);
//                if (cell == null) {
//                    cell =  sheet.getRow(i).createCell(j);
//                }
//                cell.setCellValue(i);
//            }
//        }
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\rs.xls");
//            workbook.write(out);
//            out.close();
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
