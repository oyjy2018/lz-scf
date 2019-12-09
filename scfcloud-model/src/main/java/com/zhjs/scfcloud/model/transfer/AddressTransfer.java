package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.entity.Address;
import com.zhjs.scfcloud.model.vo.AddressListVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 地址数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:20
 *
 * @author liuchanghai
 * @create 2019-05-23 14:20
 * @since
 */

@Mapper(componentModel = "spring")
public interface AddressTransfer {

    List<AddressListVO> address2AddressListVo(List<Address> list);
}
