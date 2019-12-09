package com.zhjs.scfcloud.model.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.Arrays;
import java.util.Optional;

/**
 * 用信相关枚举
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CreditUseEnum  {

    ;
    //还款状态
    public enum RefundStatus{
        status0(0, "正常"),
        status1(1, "逾期"),
        status2(2, "已结清");

        private Integer status;
        private String desc;

        RefundStatus(final Integer status, final String desc) {
            this.status = status;
            this.desc = desc;
        }

        public static String getRefundStatusDesc(Integer status){
            Optional<String> refundStatusDesc = Arrays.stream(RefundStatus.values())
                    .filter(refundStatus -> refundStatus.getStatus().intValue() == status)
                    .map(refundStatus -> refundStatus.getDesc()).findAny();
            return refundStatusDesc.orElse("");
        }

        public String getDesc(){
            return this.desc;
        }


        public Integer getStatus(){
            return this.status;
        }
    }
    ;
    //用信类型
    public enum UseType{
        useType0(0, "开商票"),
        useType1(1, "工程贷");

        private Integer useType;
        private String useTypeName;

        UseType(Integer useType, String useTypeName) {
            this.useType = useType;
            this.useTypeName = useTypeName;
        }

        public static String getUseTypeName(Integer type){
            Optional<String> useTypeName = Arrays.stream(UseType.values())
                    .filter(useType -> useType.getUseType().intValue() == type)
                    .map(useType -> useType.getUseTypeName()).findAny();
            return useTypeName.orElse("");
        }

        public Integer getUseType() {
            return useType;
        }

        public String getUseTypeName() {
            return useTypeName;
        }
    }
    ;
    //商票状态
    public enum TicketStatus{
        ticketStatus0(0, "已开票"),
        ticketStatus1(1, "已收票"),
        ticketStatus2(2, "提示付款中"),
        ticketStatus3(3, "付款签收中"),
        ticketStatus4(4, "已解付"),
        ticketStatus5(5, "贴现申请中"),
        ticketStatus6(6, "背书签收中"),
        ticketStatus7(7, "背书已签收"),
        ;

        private Integer status;
        private String name;

        TicketStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public static String getName(Integer status){
            Optional<String> name = Arrays.stream(TicketStatus.values())
                    .filter(ticketStatus -> ticketStatus.getStatus().intValue() == status)
                    .map(ticketStatus -> ticketStatus.getName()).findAny();
            return name.orElse("");
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }


}
