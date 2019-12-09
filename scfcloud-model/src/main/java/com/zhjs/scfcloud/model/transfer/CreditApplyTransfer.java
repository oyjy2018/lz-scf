package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.entity.CreditApplyAudit;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyMyListAppVO;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyMyListWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditAuditVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 客户数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 16:29
 *
 * @author liuchanghai
 * @create 2019-06-11 16:29
 * @since
 */

@Mapper(componentModel = "spring")
public interface CreditApplyTransfer {

    List<CreditApplyMyListAppVO>  toCreditApplyMyListAppVO(List<CreditApplyMyListWebVO> creditApplyMyListWebVOList);

    CreditApplyMyListAppVO  toCreditApplyMyListAppVO(CreditApplyMyListWebVO creditApplyMyListWebVO);

    List<CreditAuditVO> creditApplyAudit2CreditAuditVO(List<CreditApplyAudit> list);
}
