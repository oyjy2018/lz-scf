package com.zhjs.scfcloud.util.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author: dailongting
 * @date:2019/5/21 11:31
 */
@Component
public class FileUtil {

    private static String fileServiceUrl;

    @Value("${file.service.url}")
    public void setFileServiceUrl(String fileServiceUrl){
        FileUtil.fileServiceUrl = fileServiceUrl;
    }

    public static String getFileServiceUrl() {
        return fileServiceUrl;
    }


    /**
     * 获取文件预览地址
     * @param fileUrl
     * @return
     */
    public static String getViewFileUrl(String fileUrl){
        return getFileServiceUrl()+"file/viewFile?fileUrl="+fileUrl;
    }

    /**
     * 获取文件下载地址
     * @param fileUrl
     * @return
     */
    public static String getDownloadFileUrl(String fileUrl){
        return getFileServiceUrl()+"file/downloadFile?fileUrl="+fileUrl;
    }

    public static String getNewFileName(String suffix){
        return UUID.randomUUID().toString() + suffix;
    }

    /**
        * 获取文件名称
        * @param headerStr
        * @return
        */
    public static String getfilename(String headerStr){
        return headerStr.substring(headerStr.lastIndexOf("/")+1,headerStr.lastIndexOf("."));
    }
    /**
    * 获取文件后缀
    */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }

    /**
     * 获取文件大小
     * @param size
     * @return
     */
    public static String getFileSize(Long size){
        //获取到的size为：1705230
        long KB = 1024;
        long MB = KB * 1024;
        long GB = MB * 1024;
        //格式化小数
        DecimalFormat df = new DecimalFormat("0.00");
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }

    /**
     * 分局文件后缀名获取文件归类 目前分三类 word/image/excel/ppt/pdf/other
     * @param fileSux 文件后缀名
     * @return
     */
    public static String getFileClassify(String fileSux){
        if(fileSux.contains(".")){
            fileSux = fileSux.substring(fileSux.lastIndexOf(".") + 1);
        }
        System.out.println("文件后缀名: " + fileSux);
        if ("doc".equals(fileSux.toLowerCase())|| "docx".equals(fileSux.toLowerCase()) ){
            return "word" ;
        }
        if ("xls".equals(fileSux.toLowerCase()) || "xlsx".equals(fileSux.toLowerCase())){
            return "excel" ;
        }
        if ("ppt".equals(fileSux.toLowerCase()) || "pptx".equals(fileSux.toLowerCase())){
            return "ppt" ;
        }
        if ("pdf".equals(fileSux.toLowerCase())){
            return "pdf" ;
        }
        if ("txt".equals(fileSux.toLowerCase())){
            return "txt" ;
        }
        if ("jpg".equals(fileSux.toLowerCase()) || "png".equals(fileSux.toLowerCase()) || "jpeg".equals(fileSux.toLowerCase()) || "gif".equals(fileSux.toLowerCase()) || "bmp".equals(fileSux.toLowerCase())){
            return "image";
        }
        if ("rar".equals(fileSux.toLowerCase())){
            return "rar";
        }
        if ("zip".equals(fileSux.toLowerCase())){
            return "zip";
        }
        return "other";
    }

    //将输入流转为字节数组
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    //将字节数组写入文件
    public static File writeBytesToFile(byte[] b, String outputFile) {
        File file = null;
        FileOutputStream os = null;

        try {
            file = new File(outputFile);
            os = new FileOutputStream(file);
            os.write(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }
        return file;
    }

    public static void main(String[] args) {
        System.out.println(getFileClassify("dslafjdskal.png"));
    }
}
