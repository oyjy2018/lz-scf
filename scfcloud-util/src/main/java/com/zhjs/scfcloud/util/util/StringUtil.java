package com.zhjs.scfcloud.util.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author: dailongting
 * @date:2019/5/22 15:14
 */
public class StringUtil {

    /**
     * 正则表达式校验
     * @param val
     * @param regex
     * @return
     */
    public static boolean regexValid(String val, String regex){
        if(val == null || isEmpty(regex)) return false;

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(val).matches();
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        if(StringUtils.isEmpty(str)) return false;

        Boolean isEmail = false;
        String expr = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    public static boolean isPhone(String str) {
        if(StringUtils.isEmpty(str)) return false;

        Pattern pattern = Pattern.compile("^[\\d]{11}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 个人用户密码规则（必须包含大写字母、小写字母、数字、符号中至少3种）
     * @param password
     * @return
     */
    public static boolean matchPwdReg(String password) {
        if(StringUtils.isEmpty(password)) return false;
        int kindCount = 0;//类型数量
        //有大写字母
        if (password.matches(".*[A-Z]+.*"))
            kindCount++;
        //有小写字母
        if (password.matches(".*[a-z]+.*"))
            kindCount++;
        //有数字
        if (password.matches(".*[0-9]+.*"))
            kindCount++;
        //有特殊符号
        if (password.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*"))
            kindCount++;
        //类型不小于3时符合要求
        if (kindCount >= 2)
            return true;
        return false;
    }

    public static boolean isEmpty(Object o){
        return o == null || "".equals(o.toString());
    }

    /**
     * 转驼峰式命名
     * @param name
     * @return
     */
    public static String toCamelName(String name){
        if(isEmpty(name)) return null;

        if(!name.contains("_")) return name;

        StringBuffer sb = new StringBuffer();
        String[] camels = name.split("_");
        for(String camel:camels){
            if(camel.isEmpty()) continue;
            if(sb.length() == 0){
                sb.append(camel.toLowerCase());
            }else{
                sb.append(camel.substring(0,1).toUpperCase());
                sb.append(camel.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 转成下划线格式命名
     * @param name
     * @return
     */
    public static String toUnderlineName(String name){
        if(isEmpty(name)) return null;

        StringBuffer sb = new StringBuffer();
        String s = null;
        for(int i = 0;i<name.length();i++){

            s = name.substring(i,i+1);
            if(s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))){
                sb.append("_");
            }
            sb.append(s.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 字符串去重
     * @param string 字符串
     * @param regex 分隔符
     * @return
     */
    public static String stringDistinct(String string, String regex){
        if (StringUtil.isEmpty(string)) return string;
        String[] strs= string.split(regex);
        Set<String> set2 = new HashSet<>();
        Collections.addAll(set2, strs);
        String join = String.join(regex, set2);
        return join;
    }

    //比较两个obj的值是否相等
    public static boolean isEquals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) return true;
        if (obj1 == null && obj2 != null) return isEmpty(obj2.toString());
        if (obj1 != null && obj2 == null) return isEmpty(obj1.toString());
        if (obj1 != null && obj2 != null) return obj1.toString().equals(obj2.toString());
        //以上情况涵盖所有可能 此处return无实际意义 只是为避免报错
        return true;
    }

    //判断两个字符串 是否相等  (如果都是数字类型的  判断数值是否相等)
    public static boolean isDigitEquals(Object obj1, Object obj2) {
        if (isEmpty(obj1) && isEmpty(obj2) ){  //都为空即相等
            return true;
        }else if (!isEmpty(obj1) && !isEmpty(obj2)){ //都不为空时
            if (obj1.toString().equals(obj2.toString())){ //字符串相等
                return true;
            }else if (isDigit(obj1) && isDigit(obj2)){ //都为数值时  比较值大小是否相等
                return new BigDecimal(obj1.toString()).compareTo(new BigDecimal(obj2.toString())) == 0 ? true:false;
            }else {
               return false;
            }
        }else {
            return false;
        }
    }

    //校验是否为数字(可为小数)
    public static boolean isDigit(Object object) {
        if (object == null) return false;
        if (object.toString().matches("^\\d+(\\.\\d+)?$")) return true;
        return false;
    }

    //校验是否为数字(不能为小数)
    public static boolean isNum(Object object) {
        if (object == null) return false;
        if (object.toString().matches("^\\d+?$")) return true;
        return false;
    }

    private static Map<String, String> city = new HashMap<String, String>(){{
        put("11","北京");put("12","天津");put("13","河北");put("14","山西");put("15","内蒙古");put("21","辽宁");
        put("22","吉林");put("23","黑龙江");put("31","上海");put("32","江苏");put("33","浙江");put("34","安徽");
        put("35","福建");put("36","江西");put("37","山东");put("41","河南");put("42","湖北");put("43","湖南");
        put("44","广东");put("45","广西");put("46","海南");put("50","重庆");put("51","四川");put("52","贵州");
        put("53","云南");put("54","西藏");put("61","陕西");put("62","甘肃");put("63","青海");put("64","宁夏");
        put("65","新疆");put("71","台湾");put("81","香港");put("82","澳门");put("91","国外");
    }};

    /**
     * 身份证校验
     * @param idCard
     * @return
     */
    public static boolean identityCodeValid(String idCard) {
        boolean pass = true;
        String regex = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)";

        if(isEmpty(idCard) || !regexValid(idCard,regex)){
            pass = false;
        }else if(city.get(idCard.substring(0,2)) == null){
            pass = false;
        }else{
            //18位身份证需要验证最后一位校验位
            if(idCard.length() == 18){
                String[] s = idCard.split("");
                //∑(ai×Wi)(mod 11)
                // 加权因子
                int[] factor = new int[]{ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
                //校验位
                String[] parity = new String[]{ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
                int sum = 0;
                int ai = 0;
                int wi = 0;
                for (int i = 0; i < 17; i++)
                {
                    ai = Integer.valueOf(s[i]);
                    wi = factor[i];
                    sum += ai * wi;
                }
                String last = parity[sum % 11];
                if(!last.equals(s[17].toUpperCase())){
                    pass =false;
                }
            }
        }
        return pass;
    }

    /**
     * 将原对象改为目标长度的字符串
     * @param length 目标长度
     * @param fill 填充的字符串
     * @param source 原对象
     * @return
     */
    public static String changeLength(int length, String fill, Object source) {
        if (fill == null || source == null) return null;
        String sourceStr = source.toString();
        int sourceLength = sourceStr.length();
        if (length == sourceLength) {
            return sourceStr;
        }else if (length < sourceLength) { //比目标长度长的截取后几位
            return sourceStr.substring(sourceLength-length);
        }else  {//比目标长度短的在前面加上填充字符串
            for (int i = 0; i < length - sourceLength; i++) {
                sourceStr = fill + sourceStr;
            }
            return sourceStr;
        }
    }

    public static String objToString(Object o){
        if(o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * 通过身份证号码获取出生日期、性别、年龄
     * @param certificateNo
     * @return 返回的出生日期格式：1990-01-01   性别格式：女，男
     */
    public static Map<String, String> getBirAgeSex(String certificateNo) {
        String birthday = "";
        String age = "";
        String sexCode = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag) return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag) return new HashMap<String, String>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3, certificateNo.length())) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (flag && certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 4, certificateNo.length() - 1)) % 2 == 0 ? "女" : "男";
            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("age", age);
        map.put("sex", sexCode);
        return map;
    }

    /**
     * 判断一个字符串在另一个字符串中出现的次数
     * 使用indexOf和subString方法，循环判断并截取
     */
    public static int getContainCount(String refer,String match) {
        if (StringUtil.isEmpty(refer) || StringUtil.isEmpty(match)) {
            return 0;
        }
        int count = 0;
        while(refer.indexOf(match)>=0) {
            refer=refer.substring(refer.indexOf(match)+match.length());
            count++;
        }
        return count;
    }

    /**
     * 判断一个字符串在另一个字符串中
     * @param refer 参照字符串
     * @param match 匹配字符串
     * @param regex 分隔符
     * @return
     */
    public static boolean isContain(String refer,String match, String regex){
        if (StringUtil.isEmpty(refer) || StringUtil.isEmpty(match)) {
            return false;
        }
        return Stream.of(refer.split(regex)).filter(s -> match.equals(s)).findAny().isPresent();
    }

    public static void main(String[] args) {
        System.out.println(isContainChinese("李老板"));
    }

    /**
     * 获取缩略名称
     * @param name 原名称
     * @param regex 分隔符
     * @param count 取的数量
     * @return
     */
    public static String getBreviaryName(String name, String regex, int count) {
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(regex) || count <= 0) return name;
        String[] names = name.split(regex);
        if (names.length <= count) return name;
        return names[0]+","+names[1]+"...";
    }

    /**
     * 逗号分隔的字符串转lang数组
     * @param value
     * @return
     */
    public static long[] stringConvertLong(String value) {
        long[] intArr = new long[0];
        if(isEmpty(value)){
            intArr = new long[0];
        }else{
            String[] valueArr = value.split(",");
            intArr = new long[valueArr.length];
            for (int i = 0; i < valueArr.length; i++) {
                intArr[i] = Integer.parseInt(valueArr[i]);
            }
        }
        return intArr;
    }
    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 前所有空格
     * @param modelName
     * @return
     */
    public static String trimAll(String modelName) {
        if (modelName == null) return modelName;
        modelName = modelName.trim();
        modelName = modelName.replace(" ","");
        return modelName;
    }
}
