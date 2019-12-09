package com.zhjs.scfcloud.util.util;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WordUtil {

    /**
     * 读取word文件内容
     * @param path
     * @return
     */
    public static String readWord(String path) {
        String s = "";
        try {
            if(path.endsWith(".doc")) {
                InputStream is = new FileInputStream(new File(path));
                WordExtractor ex = new WordExtractor(is);
                s = ex.getText();
            }else if (path.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                s = extractor.getText();
            }else {
                System.out.println("传入的word文件不正确:"+path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        long time1 = System.currentTimeMillis();
       // String s =  readWord("F:\\svn\\10 合同\\腾讯隐私政策.docx");
        String s =  PDFUtil.getCharacterContent("C:\\Users\\Administrator\\Desktop\\template.pdf");
        System.out.println(s);
        long time2 = System.currentTimeMillis();
        System.out.println("read spend:"+(time2-time1)+"ms");
        StringUtil.getContainCount(s," 1 / 23、甲方保证其在票据上的签章真实、有效。");
        long time3 = System.currentTimeMillis();
        System.out.println("way1 spend:"+(time3-time2)+"ms");

    }
}
