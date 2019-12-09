package com.zhjs.scfcloud.util.enums;


/**
 * @author: dailongting
 * @date:2019/5/25 17:09
 */
public enum UserEnum {
    ;

    public enum PermissionType{
        PermissionType1(1,"个人"),
        PermissionType2(2,"机构");

        private Integer status;
        private String statusCH;

        PermissionType(Integer status, String statusCH) {
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
}
