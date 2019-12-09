package com.zhjs.scfcloud.util.util;

import com.zhjs.scfcloud.util.enums.EnumBean;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * 数组工具类
 */
public class ListUtil {


    /**
     * 判断list是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        if (list == null)
            return true;
        return list.isEmpty();
    }

    /**
     * 复制集合
     * @param <E>
     * @param source
     * @param destinationClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass){
        if (source.size()==0) return Collections.emptyList();
        List<E> res = new ArrayList<E>(source.size());
        for (Object o : source) {
            E e = null;
            try {
                e = destinationClass.newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            BeanUtils.copyProperties(o, e);
            res.add(e);
        }
        return res;
    }

    public static void main(String[] args) {

        EnumBean bean = new EnumBean(1,"1");
        bean.test1 = 5;
        EnumBean bean1 = new EnumBean(2,"3");
        BeanUtils.copyProperties(bean1,bean);
        System.out.println(bean.test1);
    }
}
