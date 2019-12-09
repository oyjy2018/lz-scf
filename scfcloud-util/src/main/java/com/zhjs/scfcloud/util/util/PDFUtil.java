package com.zhjs.scfcloud.util.util;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.*;
import java.util.List;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

@Slf4j
public class PDFUtil {

    private static final String NAME ="STSong-Light";
    private static final String ENCODING = "UniGB-UCS2-H";

    public static void main(String[] args) throws IOException {
        try {
//            File file = new File("F:\\aaa.pdf");
//            InputStream inputStream = new FileInputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(inputStream);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int date = -1;
//            while ((date = bis.read()) != -1) {
//                bos.write(date);
//            }
            Map map = new HashMap();
            map.put("money",2000);
            map.put("percent",5);
            ByteArrayOutputStream bos = null;
            try {
                bos = createPDF("F:\\test.pdf",map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File newfile = new File("F:\\aa.pdf");
            FileOutputStream out = new FileOutputStream(newfile);
            out.write(bos.toByteArray());
            out.close();
//            bis.close();
//            inputStream.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据模板创建PDF并写入到输出流
     */
    public static ByteArrayOutputStream createPDF(String templateUrl, Map<String, Object> data) throws Exception {
        PdfReader reader = null;
        PdfStamper stamper = null;
        AcroFields acroFields = null;
        ByteArrayOutputStream bos = null;
        //这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Set<String> keys = null;
        Object value = null;
        bos = new ByteArrayOutputStream();
        //创建一个pdf读入流

        reader = new PdfReader(FileUtil.getInputStream(templateUrl));
        //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
        stamper = new PdfStamper(reader, bos);

        //获取表单集合
        acroFields = stamper.getAcroFields();
        keys = acroFields.getFields().keySet();
        for (String key : keys) {
            value = data.get(key);
            if (value != null) {
                acroFields.setFieldProperty(key, "textfont", bf, null);
                acroFields.setField(key, data.get(key).toString());
            }
        }

        //设置表单域不可编辑
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();
        return bos;
    }

    /**
     * 根据模板创建PDF并写入到输出流
     */
    public static ByteArrayOutputStream createPDF(byte[] bytes, Map<String, Object> data) throws Exception {
        PdfReader reader = null;
        PdfStamper stamper = null;
        AcroFields acroFields = null;
        ByteArrayOutputStream bos = null;
        //这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
//      BaseFont bf = BaseFont.createFont(getChineseFont()+",1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Set<String> keys = null;
        Object value = null;
        bos = new ByteArrayOutputStream();
        //创建一个pdf读入流

        reader = new PdfReader(bytes);
        //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
        stamper = new PdfStamper(reader, bos);

        //获取表单集合
        acroFields = stamper.getAcroFields();
        keys = acroFields.getFields().keySet();
        for (String key : keys) {
            value = data.get(key);
            if (value != null) {
                acroFields.setFieldProperty(key, "textfont", bf, null);
                acroFields.setField(key, data.get(key).toString());
            }
        }

        //设置表单域不可编辑
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();

        return bos;
    }

    /**
     * 获取中文字体位置
     *
     * @author xxj 2017年4月28日
     */
    private static String getChineseFont() {
        //宋体（对应css中的 属性 font-family: SimSun; /*宋体*/）
        String font1 = "C:/Windows/Fonts/simsun.ttc";

        //判断系统类型，加载字体文件
        Properties prop = System.getProperties();
        String osName = prop.getProperty("os.name").toLowerCase();
        System.out.println(osName);
        if (osName.indexOf("linux") > -1) {
            font1 = "/usr/share/fonts/chinese/simsun.ttc";
        }
        if (!new File(font1).exists()) {
            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！" + font1);
        }
        return font1;
    }


    /**
     * 合并PDF
     */
    public static void joinPDF(List<ByteArrayOutputStream> boss, OutputStream out) throws Exception {
        PdfReader reader = null;
        PdfImportedPage pip = null;

        List<PdfReader> readers = new ArrayList<>();
        Document doc = new Document();
        PdfCopy copy = new PdfCopy(doc, out);

        int tatolPage = 0;
        doc.open();
        for (ByteArrayOutputStream b : boss) {
            reader = new PdfReader(b.toByteArray());
            tatolPage = reader.getNumberOfPages();
            for (int i = 1; i <= tatolPage; i++) {
                doc.newPage();
                pip = copy.getImportedPage(reader, i);
                copy.addPage(pip);
            }
            readers.add(reader);
        }
        doc.close();
        for (PdfReader pr : readers) {
            pr.close();
        }
    }


    /**
     * 合并PDF 返回绝对路径
     *
     * @author:liuzhiyu
     * @param:
     * @date: 2018年7月10日 上午9:26:40
     * @return:String
     */
    public static String joinPDF(List<ByteArrayOutputStream> boss, String newFile) throws Exception {
        PdfReader reader = null;
        PdfImportedPage pip = null;
        Document doc = null;
        try {
            List<PdfReader> readers = new ArrayList<>();
            doc = new Document();
            PdfCopy copy = new PdfCopy(doc, new FileOutputStream(newFile));
            int tatolPage = 0;
            doc.open();
            for (ByteArrayOutputStream b : boss) {
                reader = new PdfReader(b.toByteArray());
                tatolPage = reader.getNumberOfPages();
                for (int i = 1; i <= tatolPage; i++) {
                    doc.newPage();
                    pip = copy.getImportedPage(reader, i);
                    copy.addPage(pip);
                }
                readers.add(reader);
            }
            doc.close();
            for (PdfReader pr : readers) {
                pr.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
        return newFile;
    }

    /**
     * 多个PDF文件流合成一个文件流
     */
    public static ByteArrayOutputStream mergePDF(List<InputStream> pdfStreamList) {
        try {
            ByteArrayOutputStream allPdfStream = new ByteArrayOutputStream();
            PDFMergerUtility utility = new PDFMergerUtility();
            utility.setDestinationStream(allPdfStream);
            utility.addSources(pdfStreamList);
            utility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            return allPdfStream;
        } catch (IOException e) {
            log.error("合同合并失败 e:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 生成一个段落
     * @param data 段落的文本和数据
     * @param fontLabel 文本块中文本的字体
     * @param fontValue 文本块中数据的字体
     * @param document  文档
     * @return Paragraph
     */
    public static void createParagraph(List<List<Map<String,String>>> data,Font fontLabel,Font fontValue,Document document) throws DocumentException {

        for(int row=0;row<data.size();row++){
            Paragraph paragraph = new Paragraph(20);
            List<Map<String,String>> colmnList = data.get(row);//一行的列
            for(int colmn=0;colmn<colmnList.size();colmn++){
                Map<String,String> labelValueMap = colmnList.get(colmn);
                for(Map.Entry<String,String> labelValue : labelValueMap.entrySet()){
                    //文本块，如姓名:小明
                    Chunk chunkLable = new Chunk(labelValue.getKey(), fontLabel);
                    Chunk chunkValue = new Chunk(labelValue.getValue(), fontValue);
//                    chunkValue.setWordSpacing(100F);
                    /*
                     * 设置块往上移动的距离（上标），如果是负数就表示是往下移动（下标）
                     * 注意，块所在文档中的位置还存在
                     * 文本值在下划线上一点
                     */
                    chunkValue.setTextRise(1);
                    //第一个参数：下划线粗细， 第二个参数：下划线Y轴位置
                    chunkValue.setUnderline(0.5f, -3f);

                    Phrase party = new Phrase();
                    party.add(chunkLable);
                    party.add(chunkValue);
                    paragraph.add(party);//将文本块加入到段落中
                }
            }
            document.add(paragraph);
        }
    }

    public static void createTable(String[] tableHeads,int[] columnWidths,List<List<String>> tableDataList,Font contentFontFirst,Document document) throws DocumentException {
        PdfPTable policyPlanTable = new PdfPTable(tableHeads.length); //设置表格有多少列

        policyPlanTable.setWidths(columnWidths);
        policyPlanTable.setWidthPercentage(100);//占页面宽度比例
        //设置表格中单元格的属性,所有单元格都可以使用
        PdfPCell pdfPCell = new PdfPCell();//创建一个单元格
        //设置字体水平居中
        pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//设置水平对齐
        //设置字体垂直对齐居中
        pdfPCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //保险方案内容：
        for(String head : tableHeads){
            pdfPCell.setPhrase(new Phrase(head , contentFontFirst));
            policyPlanTable.addCell(pdfPCell);
        }
        policyPlanTable.completeRow();

        for(List<String> strList : tableDataList){
            int column=0;
            for(String value : strList){
                PdfPCell pdfPCellA = new PdfPCell();
                //设置字体水平居中
                pdfPCellA.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);//设置水平对齐
                //设置字体垂直对齐居中
                pdfPCellA.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                if(tableHeads.length==10){ //合同信息
                    if(column == 2 || column == 3 || column == 9){//服务产品名称、其他说明左对齐
                        //设置字体水平居中
                        pdfPCellA.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);//设置水平对齐
                    }
                    if(column > 3 && column< 9){//服务产品名称后面的数量金额、说明右对齐
                        //设置字体水平居中
                        pdfPCellA.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);//设置水平对齐
                    }
                } else if(tableHeads.length==7){//案件信息
                    if(column == 2 || column == 6){//服务产品名称、其他说明左对齐
                        //设置字体水平居中
                        pdfPCellA.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);//设置水平对齐
                    }
                    if(column ==3 || column ==4 || column ==5 ){//案件金额、尾款或比例、案件号
                        //设置字体水平居中
                        pdfPCellA.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);//设置水平对齐
                    }
                }
                pdfPCellA.setPhrase(new Phrase(value, contentFontFirst));
                policyPlanTable.addCell(pdfPCellA);
                column++;
            }
            policyPlanTable.completeRow();//完成行
        }
        document.add(policyPlanTable);
    }

    /**
     * 获取字体
     * @param name 字体名称
     * @param encoding 编码
     */
    public static BaseFont getBaseFont(String name,String encoding) {
        try {
            BaseFont baseFont = BaseFont.createFont(name, encoding, BaseFont.NOT_EMBEDDED);
            return baseFont;
        } catch (Exception e) {
            log.error("getBaseFont error={}", e.getMessage());
            try {
                return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取字体,默认
     */
    public static BaseFont getBaseFont() {
        try {
            BaseFont baseFont = BaseFont.createFont(NAME, ENCODING, BaseFont.NOT_EMBEDDED);
            return baseFont;
        } catch (Exception e) {
            log.error("getBaseFont error={}", e.getMessage());
            try {
                return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取字体
     * @param name  字体名称
     * @param encoding 编码
     * @param fontSize 字体大小
     * @param style 字体样式
     * @return
     */
    public static Font getFont(String name,String encoding,int fontSize,int style ) {
        return new  Font(getBaseFont(name,encoding) , fontSize , style);
    }

    /**
     * 获取字体
     * @param baseFont  字体
     * @param fontSize 字体大小
     * @param style 字体样式
     * @return
     */
    public static Font getFont(BaseFont baseFont,int fontSize,int style ) {
        return new  Font(baseFont , fontSize , style);
    }


    /**
     * 加水印
     * @param imagePath
     * @throws Exception
     */
    public static void addImage(String imagePath,String srcFile,String destFile) {
        // 待加水印的文件
        PdfReader reader = null;
        // 加完水印的文件
        PdfStamper stamper = null;
        try{
            reader = new PdfReader(srcFile);
            stamper = new PdfStamper(reader, new FileOutputStream(destFile));
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte waterMar =null;
            PdfGState gs = null;
            // 设置水印透明度
            gs = new PdfGState();
            // 设置笔触字体不透明度为0.4f
            gs.setStrokeOpacity(0.4f);
            Image image = Image.getInstance(imagePath);
            // 设置坐标 绝对位置 X Y
            image.setAbsolutePosition(20, 30);
            // 设置旋转弧度
            image.setRotation(20);// 旋转 弧度
            // 设置旋转角度
            image.setRotationDegrees(20);// 旋转 角度
            // 设置等比缩放
            image.scalePercent(90);// 依照比例缩放
            // 循环对每页插入水印
            for (int i = 1; i < total; i++) {
                // 加入水印
                waterMar = stamper.getUnderContent(i);
                // 开始设置水印
                waterMar.beginText();
                // image.scaleAbsolute(200,100);//自定义大小
                // 设置透明度
                waterMar.setGState(gs);
                // 添加水印图片
                waterMar.addImage(image);
                //结束设置
                waterMar.endText();
                waterMar.stroke();
            }
        }catch (Exception e){
            log.error("添加水印异常={}",e);
        }finally {
            if(stamper!=null){
                try {
                    stamper.close();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(reader!=null){reader.close();}
        }
    }

    /**
     *
     * 【功能描述：添加图片和文字水印】 【功能详细描述：功能详细描述】
     * @param srcFile 待加水印文件
     * @param destFile 加水印后存放地址
     * @param text 加水印的文本内容
     * @param textWidth 文字横坐标
     * @param textHeight 文字纵坐标
     * @throws Exception
     */
    public void addWaterMark(String srcFile, String destFile, String text,
                             int textWidth, int textHeight) throws Exception
    {
        // 待加水印的文件
        PdfReader reader = new PdfReader(srcFile);
        // 加完水印的文件
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        // 设置字体
        BaseFont font = BaseFont.createFont();
        // 循环对每页插入水印
        for (int i = 1; i < total; i++)
        {
            // 水印的起始
            content = stamper.getUnderContent(i);
            // 开始
            content.beginText();
            // 设置颜色 默认为蓝色
            content.setColorFill(BaseColor.BLUE);
            // content.setColorFill(Color.GRAY);
            // 设置字体及字号
            content.setFontAndSize(font, 38);
            // 设置起始位置
            // content.setTextMatrix(400, 880);
            content.setTextMatrix(textWidth, textHeight);
            // 开始写入水印
            content.showTextAligned(Element.ALIGN_LEFT, text, textWidth, textHeight, 45);
            content.endText();
        }
        stamper.close();
    }


    /**
     * 根据模板创建PDF并写入到输出流
     */
    public static ByteArrayOutputStream createPDF2(byte[] bytes, Map<String, Object> data,String fileName) throws Exception {
        PdfReader reader = null;
        PdfStamper stamper = null;
        AcroFields acroFields = null;
        ByteArrayOutputStream bos = null;
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Set<String> keys = null;
        Object value = null;
        bos = new ByteArrayOutputStream();
        reader = new PdfReader(bytes);//创建一个pdf读入流
        //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
        stamper = new PdfStamper(reader, bos);

        //获取表单集合
        acroFields = stamper.getAcroFields();
        keys = acroFields.getFields().keySet();
        for (String key : keys) {
            value = data.get(key);
            if (value != null) {
                acroFields.setFieldProperty(key, "textfont", bf, null);
                acroFields.setField(key, data.get(key).toString());
            }
        }
        Map<String, List<List<String>>> listMap =  (Map<String, List<List<String>>>) data.get("list");
        for (String key : listMap.keySet()) {
            List<List<String>> lists = listMap.get(key);
            int pageNo = acroFields.getFieldPositions(key).get(0).page;
            PdfContentByte pcb = stamper.getOverContent(pageNo);
            Rectangle signRect = acroFields.getFieldPositions(key).get(0).position;
            //表格位置
            int column = lists.get(0).size();
            int row = lists.size();

            PdfPTable table =  new PdfPTable(column);
            float tatalWidth = signRect.getRight() - signRect.getLeft() - 1;
            int size = lists.get(0).size();
            float width[] = new float[size];
            for(int i=0;i<size;i++){
                if(i==0){
                    width[i]=60f;
                }else{
                    width[i]=(tatalWidth-60)/(size-1);
                }
            }
            table.setTotalWidth(width);
            table.setLockedWidth(true);
            table.setKeepTogether(true);
            table.setSplitLate(false);
            table.setSplitRows(true);
            Font FontProve = new Font(bf, 10, 0);
            //表格数据填写
            for(int i=0;i<row;i++){
                List<String> list = lists.get(i);

                for(int j=0;j<column;j++){
                    String val="";
                    if(j<list.size()){
                        val=String.valueOf(list.get(j));
                    }
                    Paragraph paragraph = new Paragraph(val, FontProve);
                    PdfPCell cell = new PdfPCell(paragraph);
                    cell.setBorderWidth(0.6f);
                    if(list.size()>=6){
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        if(j==2){
                            cell.setVerticalAlignment(Element.ALIGN_LEFT);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        }else if(j>2){
                            cell.setVerticalAlignment(Element.ALIGN_RIGHT);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        }
                    }
                    if(j==5 && list.size()==6){
                        cell.setColspan(2);
                    }else if(list.size()==1 && (val.contains("案件信息"))){
                        cell.setColspan(7);
                        cell.setBorder(0);
                    }else if(list.size()==2 && j==0 && (list.get(j).contains("专利申报特殊要求"))){
                        cell.setColspan(2);
                        cell.setBorder(0);
                    }else if(list.size()==2 && j==1 && (list.get(j-1).contains("专利申报特殊要求"))){
                        cell.setColspan(5);
                        cell.setBorder(0);
                    }
                    cell.setLeading(0, (float) 1.4);
                    table.addCell(cell);
                    if(j==5 && list.size()==6){
                        break;
                    }else if(list.size()==1){
                        break;
                    }
                }
            }
            table.writeSelectedRows(0, -1, signRect.getLeft(), signRect.getTop(), pcb);
        }
        stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
        stamper.close();

        Document doc = new Document(PageSize.A4, 10, 10, 50, 10);
        FileOutputStream out = new FileOutputStream(fileName);// 输出流
        PdfCopy copy = new PdfCopy(doc, out);
        doc.open();
        int pageNum = reader.getNumberOfPages();
        for(int i = 1;i <= pageNum;i++){
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
            copy.addPage(importPage);
        }
        doc.close();

        return bos;
    }

    /**
     * 获取pdf文字内容
     * @param pdfPath
     * @return
     */
    public static String getCharacterContent(String pdfPath){
        PdfReader reader = null;
        StringBuffer buff = new StringBuffer();
        try {
            reader = new PdfReader(pdfPath);
            int num = reader.getNumberOfPages();// 获得页数

            for (int i = 1; i <= num; i++) {
                buff.append(PdfTextExtractor.getTextFromPage(reader, i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff.toString();
    }

}
