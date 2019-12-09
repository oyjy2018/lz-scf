package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.entity.LzProjectBasicInfo;
import com.zhjs.scfcloud.model.entity.LzProjectGathering;
import com.zhjs.scfcloud.model.entity.LzProjectPledgeCashExtract;
import com.zhjs.scfcloud.model.entity.LzProjectPledgeCashPay;
import com.zhjs.scfcloud.model.vo.lzProject.*;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 深华项目转换
 */

@Mapper(componentModel = "spring")
public interface LzProjectTransfer {
    LzProjectBasicInfoVO toLzProjectBasicInfoVO(LzProjectBasicInfo basicInfo);

    LzProjectItemInfoVO toLzProjectItemInfoVO(LzProjectBasicInfo basicInfo);

    List<LzProjectGatheringVO> toLzProjectGatheringVOList(List<LzProjectGathering> list);

    List<LzProjectPledgeCashPayVO> toLzProjectPledgeCashPayVOList(List<LzProjectPledgeCashPay> list);

    List<LzProjectPledgeCashExtractVO> toLzProjectPledgeCashExtractVOList(List<LzProjectPledgeCashExtract> list);
}
