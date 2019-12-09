package com.zhjs.scfcloud.util.util;


import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final int TIME_OUT = 100 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static final int RESPONSE_CODE_SUCCESS = 200; // 请求成功 响应码
    private final  static String METHOD_GET="GET";
    private final  static String METHOD_POST="POST";
    private final  static String METHOD_PUT="PUT";
    private final  static String METHOD_DELETE="DELETE";


    /**
     * 将文件传输到目标服务器
     * @param file  需要上传的文件
     * @param RequestURL  目标文件服务地址
     * @param keyName  目标文件服务器接收文件的参数名
     * @return
     * @throws IOException
     */
    public static String uploadFile(File file, String RequestURL,String keyName)  {
        logger.info("subject:{},url:{},keyName:{}","上传文件",RequestURL,keyName);
        String result = null;
        String BOUNDARY = "letv"; // 边界标识 随机生成
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL  url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);

            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\""+keyName+"\";" +
                        " filename=\""+ file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024 * 1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int responseCode = conn.getResponseCode();
                logger.info("response code:" + responseCode);
                if (responseCode != RESPONSE_CODE_SUCCESS ) return null;
                InputStream input = conn.getInputStream();
                StringBuffer resultSb = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    resultSb.append((char) ss);
                }
                result = resultSb.toString();
                result = new String(result.getBytes("iso8859-1"), "utf-8");
                logger.info("result:{}",result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 从目标服务器下载文件
     * @param url
     * @param tmpFilePath 临时文件路径  会将文件下载到本地
     * @return
     * @throws IOException
     */
    public static File downloadFile(String url,String tmpFilePath) throws IOException {
        logger.info("subject:{},url:{},tmpFilePath:{}","下载文件",url,tmpFilePath);
        URL httpUrl=new URL(url);
        HttpURLConnection httpConn=(HttpURLConnection)httpUrl.openConnection();
        httpConn.setDoOutput(true);// 使用 URL 连接进行输出
        httpConn.setDoInput(true);// 使用 URL 连接进行输入
        httpConn.setUseCaches(false);// 忽略缓存
        httpConn.setRequestMethod("GET");// 设置URL请求方法
        //设置请求头
        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
        httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
        httpConn.setRequestProperty("Charset", "UTF-8");

        byte[] bytes = FileUtil.input2byte(httpConn.getInputStream());
        File file =FileUtil.writeBytesToFile(bytes,tmpFilePath);
        return file;
    }


    /**
     * 向指定URL发送GET方法的请求
     * @author hejiangping
     * @param url
     *            发送请求的URL
     * @param url
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return String 所代表远程资源的响应结果
     */
    public static String get(String url){
        logger.info("subject:{},url:{}","get请求",url);
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /*for (String key : map.keySet()) {
               logger.info(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        logger.info("result:{}",result);
        return result;
    }

    /**
     * post请求  以json方式传参
     * @param url url中可以带参数
     * @param json json格式字符串 可以为null
     * @return
     */
    public static String post(String url,String json){
        logger.info("subject:{},url:{},json:{}","post请求",url,json);
        HttpURLConnection conn = null;
        PrintWriter pw = null ;
        BufferedReader rd = null ;
        StringBuilder sb = new StringBuilder();
        String line = null ;
        String result = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod(METHOD_POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(json);
            pw.flush();
            rd  = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null ) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(pw != null){
                    pw.close();
                }
                if(rd != null){
                    rd.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("result:{}",result);
        return result;
    }


    public static void main(String[] args) {
        testDownloadFile();
    }

    public static void testDownloadFile(){
        try {
            //File file = downloadFile("http://192.168.1.252:23000/file/downloadFile?fileUrl=/usr/local/upload/cb0bf051-b963-48b3-817e-5eae66fd5b0e.pdf&suffix=.pdf","F://this.pdf");
            File file = downloadFile("https://demoapi.lingzhuyun.com/esign-service/file/fileDownload?fileKey=20190927155718582_390054.pdf","F://this.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testUploadFile(){
        String url = "http://192.168.1.252:23000/file/upload";
        File file = new File("F:\\test.pdf");
        uploadFile(file,url,"file");

    }

    public static String testCreateOrganize(){
        String url = "http://120.79.31.91:19201/esign-service/signAcc/createOrganize";
        Map<String,String> map = new HashMap<>();
        map.put("agentIdNo","510522199403233113");
        map.put("agentName","李洁");
        map.put("name","小米卫浴有限公司");
        map.put("organCode","9135058309622813X6");
        map.put("regType","MERGE");
        // map.put("accountId","0DC706972B8D4F30BB0B1C58CFE6FB54");
//        map.put("mobile","13266862339");
//        map.put("name","欧阳建勇");
//        map.put("idType","19");
        return post(url,JsonUtil.toJSON(map));
    }

    /**
     * 通过POST请求，json格式参数请求接口
     * @param url
     * @param json
     * @param encoding
     * @return
     */
    public static String doPostJson(String url, String json,String encoding){
        logger.info("reqUrl:{},reqParam:{}",url,json);
        String result = "";
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);
        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            // 获取结果实体
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), encoding);
            }else{
                return "接口响应失败，响应码："+response.getStatusLine().getStatusCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null){
            // 释放链接
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return result;
    }
}
