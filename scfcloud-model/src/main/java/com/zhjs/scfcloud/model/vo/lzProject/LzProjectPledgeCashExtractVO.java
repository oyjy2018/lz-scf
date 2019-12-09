package com.zhjs.scfcloud.model.vo.lzProject;

import com.zhjs.scfcloud.model.entity.LzProjectPledgeCashExtract;
import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author: dailongting
 * @date:2019/11/13 15:02
 */
@Data
public class LzProjectPledgeCashExtractVO extends LzProjectPledgeCashExtract {

    public String getExtractDateStr() {
        return DateUtil.format(super.getExtractDate());
    }
}
