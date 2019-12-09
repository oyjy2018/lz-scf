package com.zhjs.scfcloud.util.enums;

/**
 * @author: weijie.chen
 */
public enum CompanyAuditEnum {
    ;

    public enum Status{
        status0(0,"待审核"),
        status1(1,"审核通过"),
        status2(2,"审核不通过");

        private Integer status;
        private String desc;

        Status(Integer status, String desc) {
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
}
