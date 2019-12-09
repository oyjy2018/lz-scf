package com.zhjs.scfcloud.util.enums;

import com.zhjs.scfcloud.util.util.EnumUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 商票相关枚举
 */
public enum BusinessTicketEnum {
    ;
    //票据状态
    public enum BillStatus implements BaseEnum{
        bill_status_0(0,"未背书")
        ,bill_status_1(1,"背书待签收")
        ,bill_status_2(2,"背书已签收")
        ;
        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        BillStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static String getStatusCH(Integer status){
            Optional<String> billStatusOptional = Arrays.stream(BillStatus.values())
                    .filter(billStatus -> billStatus.getStatus().intValue() == status)
                    .map(billStatus -> billStatus.getStatusCH()).findAny();
            return billStatusOptional.orElse("");
        }
    }

    //询价状态
    public enum InquireStatus implements BaseEnum{
        inquire_status_1(1,"待报价")
        ,inquire_status_2(2,"竞价中")
        ,inquire_status_3(3,"询价成功")
        ,inquire_status_4(4,"询价已截止")
        ,inquire_status_5(5,"询价已撤销")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        InquireStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static String getStatusCH(Integer status){
            Optional<String> inquireStatusOptional = Arrays.stream(InquireStatus.values())
                    .filter(inquireStatus -> inquireStatus.getStatus().intValue() == status)
                    .map(inquireStatus -> inquireStatus.getStatusCH()).findAny();
            return inquireStatusOptional.orElse("");
        }

        /**
         * 是否是可接受报价的状态
         * @param status
         * @return
         */
        public static boolean hasAcceptedQuota(Integer status){
            if(InquireStatus.inquire_status_1.status.intValue() != status.intValue()
                && InquireStatus.inquire_status_2.status.intValue() != status.intValue()){
                return false;
            }else{
                return true;
            }
        }
    }

    //报价状态
    public enum QuotationStatus implements BaseEnum{
        quotation_status_1(1,"待接受报价")
        ,quotation_status_2(2,"接受报价")
        ,quotation_status_3(3,"不接受报价")
        ,quotation_status_4(4,"已撤回")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        QuotationStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<QuotationStatus> getEnumByStatus(Integer status){
            for(QuotationStatus qs:QuotationStatus.values()){
                if(Objects.equals(qs.status,status)){
                    return Optional.of(qs);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * 商票交易订单状态
     */
    public enum OrderStatus{
        /**
         * 订单状态（1：待买家签合同；2：待卖家签合同；3：双方已签合同-待支付；4：支付成功-待背书；5：已背书-待签收；6：已背书-已签收；99：交易成功；97：撤销中；98：已撤销；96：撤销失败）
         */
        ORDER_STATUS_1(1,"待买家签合同")
        ,ORDER_STATUS_2(2,"待卖家签合同")
        ,ORDER_STATUS_3(3,"双方已签合同-待支付")
        ,ORDER_STATUS_4(4,"支付成功-待背书")
        ,ORDER_STATUS_5(5,"已背书-待签收")
        ,ORDER_STATUS_6(6,"已背书-已签收")
        ,order_status_96(96,"撤单失败")
        ,ORDER_STATUS_97(97,"撤销中")
        ,ORDER_STATUS_98(98,"已撤销")
        ,ORDER_STATUS_99(99,"交易成功")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        OrderStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<OrderStatus> getEnumByStatus(Integer status){
            for(OrderStatus os:OrderStatus.values()){
                if(Objects.equals(os.status,status)){
                    return Optional.of(os);
                }
            }
            return Optional.empty();
        }

        public static String getStatusCHByStatus(Integer status){
            Optional<String> statusCH =
                    Arrays.stream(OrderStatus.values())
                            .filter(os -> Objects.equals(os.status,status))
                            .map(os -> os.getStatusCH())
                            .findAny();
            return statusCH.orElse("未知");
        }

        /**
         * 判断是否是可撤销的订单状态
         * @param status
         * @return
         */
        public static boolean hasAllowRevoke(Integer status){
            if(Objects.equals(OrderStatus.ORDER_STATUS_1.getStatus(),status)
                || Objects.equals(OrderStatus.ORDER_STATUS_2.getStatus(),status)
                || Objects.equals(OrderStatus.ORDER_STATUS_3.getStatus(),status)
                || Objects.equals(OrderStatus.ORDER_STATUS_4.getStatus(),status)
                || Objects.equals(OrderStatus.ORDER_STATUS_5.getStatus(),status)){
                return true;
            }
            return false;
        }
    }

    /**
     * 商票交易订单支付状态
     */
    public enum OrderPayStatus{
        /**
         * 支付状态（1：支付中；97：支付撤销中；98：支付失败；99：支付成功；96：支付撤销失败；）
         */
        Order_Pay_Status_1(1,"支付中")
        ,Order_Pay_Status96(96,"支付撤销失败（未支付）")
        ,Order_Pay_Status97(97,"支付撤销中")
        ,Order_Pay_Status98(98,"支付失败")
        ,Order_Pay_Status_99(99,"支付成功")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        OrderPayStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<OrderPayStatus> getEnumByStatus(Integer status){
            for(OrderPayStatus ops:OrderPayStatus.values()){
                if(Objects.equals(ops.status,status)){
                    return Optional.of(ops);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * 商票交易订单打款给卖方状态
     */
    public enum OrderRemitStatus{
        /**
         * 打款给卖方状态（1：打款给卖方中；98：打款失败；99：打款成功；）
         */
        ORDER_REMIT_STATUS_1(1,"打款给卖方中")
        ,ORDER_REMIT_STATUS_98(98,"打款失败")
        ,ORDER_REMIT_STATUS_99(99,"打款成功")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        OrderRemitStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<OrderRemitStatus> getEnumByStatus(Integer status){
            for(OrderRemitStatus ors:OrderRemitStatus.values()){
                if(Objects.equals(ors.status,status)){
                    return Optional.of(ors);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * 佣金扣除状态
     */
    public enum OrderCommDeductStatus{
        /**
         * 佣金扣除状态（1：扣除平台佣金中；98：平台佣金扣除失败；99：平台佣金扣除成功；）
         */
        ORDER_COMM_DEDUCT_STATUS_1(1,"扣除平台佣金中")
        ,ORDER_COMM_DEDUCT_STATUS_98(98,"平台佣金扣除失败")
        ,ORDER_COMM_DEDUCT_STATUS_99(99,"平台佣金扣除成功")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        OrderCommDeductStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<OrderCommDeductStatus> getEnumByStatus(Integer status){
            for(OrderCommDeductStatus ocds:OrderCommDeductStatus.values()){
                if(Objects.equals(ocds.status,status)){
                    return Optional.of(ocds);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * 用户类型（1：票方；2：资方）
     */
    public enum UserType{

        USER_TYPE_1(1,"票方")
        ,USER_TYPE_2(2,"资方")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        UserType(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<UserType> getEnumByStatus(Integer status){
            for(UserType ut:UserType.values()){
                if(Objects.equals(ut.status,status)){
                    return Optional.of(ut);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * 取消类型(1:买方取消;2:卖方取消;)
     */
    public enum CancelType{
        CANCEL_TYPE_1(1,"买方取消")
        ,CANCEL_TYPE_2(2,"卖方取消")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        CancelType(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<CancelType> getEnumByStatus(Integer status){
            for(CancelType ct:CancelType.values()){
                if(Objects.equals(ct.status,status)){
                    return Optional.of(ct);
                }
            }
            return Optional.empty();
        }
    }


    /**
     * 京东智票订单状态
     */
    public enum JdOrderStatus{
        JdOrderStatus0(0,"新建")
        ,JdOrderStatus1(1,"买方支付中")
        ,JdOrderStatus2(2,"买方支付成功")
        ,JdOrderStatus3(3,"买方支付失败")
        ,JdOrderStatus4(4,"等待卖方背书")
        ,JdOrderStatus5(5,"等待买方签收")
        ,JdOrderStatus6(6,"撤单申请")
        ,JdOrderStatus7(7,"撤单成功")
        ,JdOrderStatus8(8,"撤单失败")
        ,JdOrderStatus9(9,"撤单解冻处理中")
        ,JdOrderStatus10(10,"买方已签收")
        ,JdOrderStatus11(11,"给卖方打款成功")
        ,JdOrderStatus12(12,"给卖方打款失败")
        ,JdOrderStatus13(13,"给卖方打款处理中")
        ,JdOrderStatus14(14,"撤单退款处理中")
        ,JdOrderStatus51(51,"平台佣金扣除中")
        ,JdOrderStatus52(52,"平台佣金扣除完成")
        ;

        private Integer status;
        private String statusCH;

        public Integer getStatus() {
            return status;
        }

        public String getStatusCH() {
            return statusCH;
        }

        JdOrderStatus(Integer status, String statusCH) {
            this.status = status;
            this.statusCH = statusCH;
        }

        public static Optional<JdOrderStatus> getEnumByStatus(Integer status){
            for(JdOrderStatus e:JdOrderStatus.values()){
                if(Objects.equals(e.status,status)){
                    return Optional.of(e);
                }
            }
            return Optional.empty();
        }

        /**
         * 判断是否处于领筑订单的支付中状态
         * @param status
         * @return
         */
        public static boolean hasPaying(Integer status){
            if(Objects.equals(JdOrderStatus.JdOrderStatus0.status,status)
            || Objects.equals(JdOrderStatus.JdOrderStatus1.status,status)){
                return true;
            }

            return false;
        }

        /**
         * 判断是否处于领筑订单的支付成功-待背书状态
         * @param status
         * @return
         */
        public static boolean hasWaitEndorse(Integer status){
            if(Objects.equals(JdOrderStatus.JdOrderStatus2.status,status)
            || Objects.equals(JdOrderStatus.JdOrderStatus4.status,status)){
                return true;
            }
            return false;
        }

    }
}
