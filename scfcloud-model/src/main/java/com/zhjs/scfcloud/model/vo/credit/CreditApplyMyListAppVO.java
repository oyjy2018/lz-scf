package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.Date;

@Data
public class CreditApplyMyListAppVO {
    protected Long id;

    protected Long businessTypeId;

    //授信项目名称
    protected String itemNames;

    //申请时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    protected Date applyTime;

    //状态
    protected String flowCode;

    //状态名
    protected String flowName;

}
