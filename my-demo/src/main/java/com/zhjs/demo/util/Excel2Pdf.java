package com.zhjs.demo.util;

import com.aspose.cells.License;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Excel2Pdf {
      public static boolean getLicense() {
             boolean result = false;
             try {
                        InputStream is = Excel2Pdf.class.getClassLoader().getResourceAsStream("license/aaaLicense.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
                        License aposeLic = new License();
                        aposeLic.setLicense(is);
                        result = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
              return result;
         }

    public static void excel2pdf(String Address) {

        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            File pdfFile = new File("C:\\Users\\Administrator\\Desktop\\欧阳建勇-2019年7月考勤记录.pdf");// 输出路径
            Workbook wb = new Workbook(Address);// 原始excel路径
            PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
            pdfSaveOptions.setOnePagePerSheet(true);
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            fileOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        excel2pdf("C:\\Users\\Administrator\\Desktop\\欧阳建勇-2019年7月考勤记录.xls");
      }
 }