package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;

@Data
public class CreditApplyAllListWebVO extends CreditApplyMyListWebVO{

    //手机号码
    protected String mobile;

    private String disposeRoleNames;

    private String disposeUserNames;

    private String showDisposeRoleNames;

    private String showDisposeUserNames;

    public String getShowDisposeRoleNames() {
        return StringUtil.getBreviaryName(disposeRoleNames,",",2);
    }

    public String getShowDisposeUserNames() {
        return StringUtil.getBreviaryName(disposeUserNames,",",2);
    }
}
