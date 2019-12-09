package com.zhjs.scfcloud.util.enums;

/**
 * @author: dailongting
 * @date:2019/5/25 17:51
 */
public class EnumBean {

    public final Integer status;
    public final String statusCH;

    public Integer getTest1() {
        return test1;
    }

    public void setTest1(Integer test1) {
        this.test1 = test1;
    }

    public Integer test1;

    public EnumBean(Integer status, String statusCH){
        this.status = status;
        this.statusCH = statusCH;
    }
}
