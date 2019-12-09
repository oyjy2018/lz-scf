package com.zhjs.scfcloud.util.enums;

import com.zhjs.scfcloud.util.util.SmsUtil;

/**
 * @author: dailongting
 * @date:2019/6/12 11:56
 */
public enum  CommonEnum {
    ;

    public enum QueryType{

        CREATE("create", "创建页面")
        ,VIEW("view", "查看页面")
        ,EDIT("edit", "编辑页面")
        ,AUDIT("audit", "审批页面")
        ;

        private String status;
        private String statusCH;

        private QueryType(String status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }
    }

    /**
     * 是/否的常量枚举
     */
    public enum WhetherEnum{

        YES(1, "是")
        ,NO(0, "否")
        ;

        private Integer status;
        private String statusCH;

        private WhetherEnum(Integer status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }
    }

    public enum StatusEnnum{
        status0(0, "禁用")
        ,status1(1, "启用")
        ;

        private Integer status;
        private String statusCH;

        private StatusEnnum(Integer status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }
    }

    /**
     * 短信类型
     */
    public enum SmsType{
        status1(1, "注册", SmsUtil.lzy_sms_type_S507),
        status2(2, "修改密码", SmsUtil.lzy_sms_type_S509),
        status3(3, "修改手机号", SmsUtil.lzy_sms_type_S508);

        private Integer status;
        private String statusCH;
        private String lzySmsType;

        private SmsType(Integer status, String statusCH, String lzySmsType){
            this.status = status;
            this.statusCH = statusCH;
            this.lzySmsType = lzySmsType;
        }

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        public String getLzySmsType() {
            return lzySmsType;
        }

        public static SmsType getEnumByStatus(Integer status){
            for(SmsType st:SmsType.values()){
                if(st.status.intValue() == status.intValue()){
                    return st;
                }
            }
            return null;
        }
    }
}
