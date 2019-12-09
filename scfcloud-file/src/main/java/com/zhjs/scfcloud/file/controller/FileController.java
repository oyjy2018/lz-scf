package com.zhjs.scfcloud.file.controller;

import com.zhjs.scfcloud.file.service.FileUploadLogService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.FileUploadLog;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import com.zhjs.scfcloud.util.util.Word2Pdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: dailongting
 * @date:2019/5/20 18:29
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private Logger log = LoggerFactory.getLogger(FileController.class);
    @Value("${upload-folder}")
    private String UPLOAD_FOLDER;
    @Value("${file.service.url}")
    private String fileServiceUrl;

    @Autowired
    private FileUploadLogService fileUploadLogService;

    /**
     * 文件上传
     * @return 返回的是一个 FileUploadLog
     */
    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam MultipartFile file,@RequestParam(value = "directory",required = false) String directory ){
        if (Objects.isNull(file) || file.isEmpty()) {
            log.error("文件为空");
            return Result.failure("文件为空，请重新上传").toJSON();
        }
        if (!StringUtil.isEmpty(directory) ) {
            if (!directory.matches("^(\\w+/)*\\w*$")) {
                log.error("文件夹格式不对");
                return Result.failure("文件夹格式不对，请重新上传").toJSON();
            }
            if (!directory.endsWith("/")){
                directory = directory + "/";
            }
        }
        try{
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFileName);
            String newFileName = FileUtil.getNewFileName(suffix);
            String filePath = UPLOAD_FOLDER +(directory == null ?"":directory);
            //路径不存在时 创建路径
            File pathFile = new File(filePath);
            if(!pathFile.exists()){
                pathFile.mkdirs();
            }
            String fileUrl = filePath + newFileName;
            Path path = Paths.get(fileUrl);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            log.debug("文件写入成功...");

            // 保存文件
            FileUploadLog fileUploadLog = new FileUploadLog();
            fileUploadLog.setNewFileName(newFileName)
                .setOriginalFileName(originalFileName)
                .setFileSize(FileUtil.getFileSize(file.getSize()))
                .setFileUrl(fileUrl)
                .setFileType(FileUtil.getFileClassify(suffix))
                .setSuffix(suffix)
                .setUpdateTime(LocalDateTime.now())
                .setCreateTime(LocalDateTime.now());
            boolean result = fileUploadLogService.save(fileUploadLog);

            Map<String, String> resMap = new HashMap<>();
            resMap.put("newFileName", fileUploadLog.getNewFileName());
            resMap.put("originalFileName", fileUploadLog.getOriginalFileName());
            resMap.put("fileUrl", fileUploadLog.getFileUrl());
            resMap.put("fileType", fileUploadLog.getFileType());
            resMap.put("fileSize", fileUploadLog.getFileSize());
            resMap.put("suffix", suffix);
            resMap.put("downloadFileUrl", fileServiceUrl+"file/downloadFile?fileUrl="+fileUrl);
            resMap.put("viewFileUrl", fileServiceUrl+"file/viewFile?fileUrl="+fileUrl);

            return Result.success("文件写入成功", resMap).toJSON();
        }catch (IOException e){
            e.printStackTrace();
            log.error("上传文件异常",e.getMessage());
            return Result.failure("上传文件异常，错误信息："+e.getMessage()).toJSON();
        }
    }


    /**
     * 文件上传
     * @return 返回的是 File
     */
    @PostMapping(value = "/upload2")
    public String uploadFile2(@RequestParam MultipartFile file){
        if (Objects.isNull(file) || file.isEmpty()) {
            log.error("文件为空");
            return Result.failure("文件为空，请重新上传").toJSON();
        }
        FileUploadLog upload = upload(file);
        if (upload == null){
            Result.failure("上传文件异常，错误信息：");
        }
        Map<String, String> resMap = new HashMap<>();
        resMap.put("id", "");
        resMap.put("newFileName", upload.getNewFileName());
        resMap.put("originalFileName", upload.getOriginalFileName());
        resMap.put("fileUrl", upload.getFileUrl());
        resMap.put("fileType", upload.getFileType());
        resMap.put("fileSize", upload.getFileSize());
        resMap.put("companyId", "");
        resMap.put("businessId", "");
        resMap.put("businessType", "");
        resMap.put("fileSpecies", "");
        return Result.success("文件写入成功", resMap).toJSON();
    }

    /**
     * 文件下载
     * @param fileUrl
     * @return
     */
    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadFile(@RequestParam String fileUrl){
        try {
            InputStream in = new FileInputStream(fileUrl);
            HttpHeaders headers = new HttpHeaders();
            byte[] b= null;
            ResponseEntity<byte[]> resp = null;
            try {
                b = new byte[in.available()];
                in.read(b);
                //获取文件纪录
                FileUploadLog fileUploadLog = fileUploadLogService.findByFileUrl(fileUrl);
                String fileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator)+1);
                if (fileUploadLog != null) {
                    fileName = fileUploadLog.getOriginalFileName();//获取文件原始名称
                }
                headers.setContentDispositionFormData("attachment", dealFileName(fileName));// 文件的属性，也就是文件叫什么吧
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);// 内容是字节流
                resp = new ResponseEntity<byte[]>( b, headers, HttpStatus.OK);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("文件下载异常：{}", e.getMessage());
            return new ResponseEntity<>("文件下载异常", HttpStatus.OK);
        }
    }
    public static void main(String[] args) {
        System.out.println(File.separator);
        String fileUrl = "d:\\this\\ss\\aa.png";
        System.out.println(fileUrl.substring(0,fileUrl.lastIndexOf(".")));
    }

    /**
     * 文件预览
     * @param fileUrl
     * @return
     */
    @GetMapping("/viewFile")
    public void viewFile(@RequestParam String fileUrl, HttpServletResponse response, HttpServletRequest request){
        FileUploadLog fileUploadLog = fileUploadLogService.findByFileUrl(fileUrl);
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(File.separator)+1);
        if (fileUploadLog != null) {
            fileName = fileUploadLog.getOriginalFileName();//获取文件原始名称
        }
        byte[] data = null;
        FileInputStream input = null;
        //文件类型
        String fileType = FileUtil.getFileClassify(fileUrl);
        if ("pdf".equals(fileType) || "image".equals(fileType) || "txt".equals(fileType)) {
            try {
                input = new FileInputStream(fileUrl);
            } catch (Exception e) {
                log.info("pdf处理文件异常" + e);
            }
        }else if ("word".equals(fileType)){//word文件
            //转成PDF文件
            File file = Word2Pdf.word2pdf(fileUrl,UPLOAD_FOLDER+"tmpFromWordTransfer.pdf");
            //处理名字
            fileName = fileName.substring(0,fileName.lastIndexOf(".")) + ".pdf";
            //返回文件流
            if (file != null) {
                try {
                     input = new FileInputStream(UPLOAD_FOLDER+"tmpFromWordTransfer.pdf");
                } catch (Exception e) {
                    log.info("pdf处理文件异常" + e);
                }
            }
        }else { //其他文件暂不处理
            return;
        }

        try {
            //预览时可下载  保证文件名正确
            if (request.getHeader("user-agent").toLowerCase().indexOf("firefox") > -1) {
                //火狐浏览器自己会对URL进行一次URL转码所以区别处理
                response.setHeader("Content-Disposition", "filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
            } else {
                response.setHeader("Content-Disposition", "filename=" +dealFileName(fileName));
            }
            data = new byte[input.available()];
            input.read(data);
            response.getOutputStream().write(data);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String dealFileName(String fileName) {
        fileName = fileName.replace("+","oyjy9468");
        try {
            fileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        fileName = fileName.replace("+","%20").replace("oyjy9468","+");
        return fileName;
    }


    /**
     * 上传文件
     * @param file
     * @return
     */
    public FileUploadLog upload(MultipartFile file){
        try{
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFileName);
            String newFileName = FileUtil.getNewFileName(suffix);
            String fileUrl = UPLOAD_FOLDER + newFileName;
            Path path = Paths.get(fileUrl);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            log.debug("文件写入成功...");

            // 保存文件
            FileUploadLog file1 = new FileUploadLog();
            file1.setNewFileName(newFileName)
                    .setOriginalFileName(originalFileName)
                    .setFileSize(FileUtil.getFileSize(file.getSize()))
                    .setFileUrl(fileUrl)
                    .setFileType(FileUtil.getFileClassify(suffix))
                    .setSuffix(suffix)
                    .setUpdateTime(LocalDateTime.now())
                    .setCreateTime(LocalDateTime.now());
            boolean result = fileUploadLogService.save(file1);
            return file1;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
