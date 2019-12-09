package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.entity.CreditApply;
import com.zhjs.scfcloud.model.entity.Customer;
import com.zhjs.scfcloud.model.vo.CustomerVO;
import org.mapstruct.Mapper;

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
public interface CustomerTransfer {

    Customer addDTO2Po(CustomerDTO dto);

    CustomerVO Po2CustomerVO(Customer customer);

    CustomerDTO creditApply2CustomerDTO(CreditApply creditApply);
}
