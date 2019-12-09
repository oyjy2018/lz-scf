package com.zhjs.demo.util;



import com.aspose.slides.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Ppt2Pdf {
      public static boolean getLicense() {
             boolean result = false;
             try {
                        InputStream is = Ppt2Pdf.class.getClassLoader().getResourceAsStream("license/aaaLicense.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
                        License aposeLic = new License();
                        aposeLic.setLicense(is);
                        result = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
              return result;
         }

         public static void ppt2pdf(String Address) {

            if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
                        return;
                    }
            try {
                        long old = System.currentTimeMillis();
                        File file = new File("C:\\Users\\Administrator\\Desktop\\考勤制度培训-0809.pdf");  //新建一个空白pdf文档
                        FileOutputStream os = new FileOutputStream(file);
                        Presentation pre = new Presentation(Address);                    //Address是将要被转化的word文档
                        pre.save(os, SaveFormat.Pdf);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
                        long now = System.currentTimeMillis();
                        System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
         }



    public static void main(String[] args){
        ppt2pdf("C:\\Users\\Administrator\\Desktop\\考勤制度培训-0809.pptx");
      }
 }