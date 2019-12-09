package com.zhjs.scfcloud.util.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Word2Pdf {

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Word2Pdf.class.getClassLoader().getResourceAsStream("license/word.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param sourceUrl
     * @param tmpUrl
     */
    public static File word2pdf(String sourceUrl,String tmpUrl) {
        File file = null;
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return null;
        }
        try {
            long old = System.currentTimeMillis();
            file = new File(tmpUrl);  //新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(sourceUrl);                    //sourceUrl是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }



    public static void main(String[] args){
        word2pdf("C:\\Users\\Administrator\\Desktop\\电子签名数字证书用户使用须知(17).doc","C:\\Users\\Administrator\\Desktop\\C.html");
    }
 }