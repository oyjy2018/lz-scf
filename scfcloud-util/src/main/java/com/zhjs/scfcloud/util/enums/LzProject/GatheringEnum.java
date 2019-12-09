package com.zhjs.scfcloud.util.enums.LzProject;

import java.util.Optional;

/**
 * @author: dailongting
 * @date:2019/10/30 10:44
 */
public enum GatheringEnum {
    ;

    public enum GatheringType {
        GATHERING_TYPE_1(1, "商务经理打入资金")
        ,GATHERING_TYPE_2(2, "甲方回款")
        ;

        private int status;
        private String statusCH;

        private GatheringType(int status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public int getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        public static Optional<GatheringType> getEnumByStatus(Integer status){
            if(status == null){
                return Optional.empty();
            }
            for(GatheringType st:GatheringType.values()){
                if(st.status == status.intValue()){
                    return Optional.of(st);
                }
            }
            return Optional.empty();
        }
    }
}
