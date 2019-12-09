package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.feign.CreditRecordFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ticket.QueryCreditTicketCfgDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import com.zhjs.scfcloud.model.entity.BusinessType;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import com.zhjs.scfcloud.service.CreditRecordService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/6/27 17:09
 */
@Service
public class CreditRecordServiceImpl implements CreditRecordService {
    @Autowired
    private CreditRecordFeignService creditRecordFeignService;


    @Override
    public Result findPersonalCredit(Long creditRecordId) {
        return creditRecordFeignService.findPersonalCredit(creditRecordId);
    }

}
