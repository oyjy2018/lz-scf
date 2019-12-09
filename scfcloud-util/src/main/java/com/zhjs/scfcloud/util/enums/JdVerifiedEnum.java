package com.zhjs.scfcloud.util.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 京东开放平台相关状态
 * @author: weijie.chen
 * @date:2019/7/6 15:00
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JdVerifiedEnum{
    ;
    //实名状态
    public enum RealStatus{
        //0:未实名 4:实名成功 5:实名失效
        status1(0,"未实名"),
        status2(4,"实名成功"),
        status3(5,"实名失效");

        private Integer status;
        private String desc;

        RealStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    //账户状态
    public enum AccountStatus{
        //1:认证中 2:认证成功 3:认证失败 4:认证过期
        status1(1,"认证中"),
        status2(2,"认证成功"),
        status3(3,"认证失败"),
        status4(4,"认证过期");

        private Integer status;
        private String desc;

        AccountStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    //账户是否授权ECDS状态（0：未认证；1：已认证）
    public enum EcdsType{

        type_0(0,"未认证"),
        type_1(1,"已认证");

        private Integer status;
        private String desc;

        EcdsType(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    //打款认证状态
    public enum BankSubStatus{
        status_11("11","待发起代付"),//表示待打款
        status_12("12","待打款"),//表示待打款
        status_13("13","打款成功"),
        status_21("21","代付失败"),//表示打款失败
        status_22("22","打款失败"),//表示打款失败
        status_23("23","金额确认失败"),
        status_24("24","打款申请失败次数超限"),
        status_25("25","金额验证失败次数超限"),
        status_30("30","退票"),//极端情况下打款失败
        status_40("40","金额确认成功"),
        status_50("50","认证过期");

        private String status;
        private String desc;

        BankSubStatus(String status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
