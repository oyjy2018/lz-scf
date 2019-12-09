package com.zhjs.scfcloud.util.enums;

/**
 * @author: dailongting
 * @date:2019/9/9 17:21
 */
public enum BusinessFlowEnum {
    ;

    public enum FlowType{

        FLOW_TYPE0(0, "草稿")
        ,FLOW_TYPE1(1, "起始流程")
        ,FLOW_TYPE2(2, "进行中")
        ,FLOW_TYPE10(10, "结束流程-成功")
        ,FLOW_TYPE11(11, "结束流程-失败")
        ;

        private Integer status;
        private String statusCH;

        private FlowType(Integer status, String statusCH){
            this.status = status;
            this.statusCH = statusCH;
        }

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        /**
         * 判断是否是正向流程
         * @param status
         * @return
         */
        public static boolean hasForwardFlow(Integer status){
            if(status == null) return false;

            if(FlowType.FLOW_TYPE0.getStatus().intValue() == status.intValue()
            ||FlowType.FLOW_TYPE1.getStatus().intValue() == status.intValue()
            || FlowType.FLOW_TYPE2.getStatus().intValue() == status.intValue()
            || FlowType.FLOW_TYPE10.getStatus().intValue() == status.intValue()){
                return true;
            }

            return false;
        }
    }
}
