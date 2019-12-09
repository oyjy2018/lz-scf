package com.zhjs.scfcloud.util.constant;

/**
 * @author: dailongting
 * @date:2019/6/12 14:19
 */
public class RedisConstant {
    /**
     * redis数据存储区key
     */
    public static final String REDIS_KEY = "redis:data:";
    /**
     * 数据字典类别存储区key
     */
    public static final String REDIS_KEY_DICTIONARY = REDIS_KEY+"dictionary:";
    /**
     * 数据字典类别详情存储区
     */
    public static final String REDIS_KEY_DICTIONARY_DETAILS = REDIS_KEY+"dictionary_details:";


    public static final String REDIS_KEY_BUSINESS = REDIS_KEY+"business:";

    public static final String REDIS_KEY_BUSINESS_ATTR_VAL = REDIS_KEY_BUSINESS + "attr_val";

    public static final String REDIS_KEY_BUSINESS_FEE_FORMULA = REDIS_KEY_BUSINESS + "fee_formula";

    public static final String REDIS_KEY_BUSINESS_FLOW = REDIS_KEY_BUSINESS + "flow";

    public static final String REDIS_KEY_BUSINESS_FLOW_INTERFACE = REDIS_KEY_BUSINESS + "flow_interface";

    public static final String REDIS_KEY_BUSINESS_FLOW_PERMIT = REDIS_KEY_BUSINESS + "flow_permit";

    public static final String REDIS_KEY_BUSINESS_TYPE = REDIS_KEY_BUSINESS + "type";

    public static final String REDIS_KEY_BUSINESS_WORK_FLOW = REDIS_KEY_BUSINESS + "work_flow";

    public static final String REDIS_KEY_BUSINESS_WORK_FLOW_EXTEND = REDIS_KEY_BUSINESS + "work_flow_extend";

    public static final String REDIS_KEY_BUSINESS_WORK_FLOW_ATTR = REDIS_KEY_BUSINESS + "work_flow_attr";

    /**
     * 业务属性配置
     */
    public static final String REDIS_KEY_BUSINESS_ATTR = REDIS_KEY_BUSINESS + "attr:";

    /**
     * 业务附件配置
     */
    public static final String REDIS_KEY_BUSINESS_FILE_CFG = REDIS_KEY_BUSINESS + "file_cfg:";

    /**
     * 流程扭转附件配置
     */
    public static final String REDIS_KEY_BUSINESS_WORKFLOW_FILE_CFG = REDIS_KEY_BUSINESS + "workflow_file_cfg:";
}
