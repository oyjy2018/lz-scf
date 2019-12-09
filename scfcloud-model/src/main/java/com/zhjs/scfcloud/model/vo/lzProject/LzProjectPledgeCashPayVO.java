package com.zhjs.scfcloud.model.vo.lzProject;

import com.zhjs.scfcloud.model.entity.LzProjectPledgeCashPay;
import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author: dailongting
 * @date:2019/11/13 14:35
 */
@Data
public class LzProjectPledgeCashPayVO extends LzProjectPledgeCashPay {

    public String getPayDateStr() {
        return DateUtil.format(super.getPayDate());
    }
}
