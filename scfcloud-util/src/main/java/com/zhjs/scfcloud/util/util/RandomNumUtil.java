package com.zhjs.scfcloud.util.util;

/**
 * 生成随机数工具类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-05 12:49
 *
 * @author liuchanghai
 * @create 2019-06-05 12:49
 * @since
 */
public class RandomNumUtil {

    /**
     * 生成随机验证码
     * @param num 位数
     * @return
     */
    public static String createRandomNum(int num){
        String randomNumStr = "";
        for(int i = 0; i < num;i ++){
            int randomNum = (int)(Math.random() * 10);
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }
}
