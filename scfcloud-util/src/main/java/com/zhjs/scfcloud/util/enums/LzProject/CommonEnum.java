package com.zhjs.scfcloud.util.enums.LzProject;

import java.util.Optional;

/**
 * @author: dailongting
 * @date:2019/10/31 15:37
 */
public enum CommonEnum {
    ;

    public enum FeeType {
        FEE_TYPE_1(1, "材料费")
        ,FEE_TYPE_2(2, "人工费")
        ,FEE_TYPE_3(2, "项目管理人员薪酬")
        ,FEE_TYPE_4(2, "项目费用")
        ,FEE_TYPE_5(2, "咨询服务费")
        ,FEE_TYPE_6(2, "综合税费")
        ,FEE_TYPE_7(2, "平台服务费")
        ,FEE_TYPE_8(2, "商务服务费")
        ,FEE_TYPE_9(2, "金融服务费")
        ,FEE_TYPE_10(2, "技术服务费")
        ;

        private int status;
        private String statusCH;

        private FeeType(int status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public int getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        public static Optional<FeeType> getEnumByStatus(Integer status){
            if(status == null){
                return Optional.empty();
            }
            for(FeeType ft:FeeType.values()){
                if(ft.status == status.intValue()){
                    return Optional.of(ft);
                }
            }
            return Optional.empty();
        }

        /**
         * 是否是成本费用类型
         * @param statusCH
         * @return
         */
        public static boolean hasCostType(String statusCH){
            if(statusCH == null) {
                return false;
            }

            if(FeeType.FEE_TYPE_1.statusCH.equals(statusCH)
                    || FeeType.FEE_TYPE_2.statusCH.equals(statusCH)
                    || FeeType.FEE_TYPE_3.statusCH.equals(statusCH)
                    || FeeType.FEE_TYPE_4.statusCH.equals(statusCH)
                    || FeeType.FEE_TYPE_5.statusCH.equals(statusCH)) {
                return true;
            }

            return false;
        }
    }
}
