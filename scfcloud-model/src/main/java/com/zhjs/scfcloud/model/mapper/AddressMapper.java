package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Address;
import com.zhjs.scfcloud.model.vo.AddressListVO;

import java.util.List;

/**
 * 地址信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:05
 *
 * @author liuchanghai
 * @create 2019-05-23 14:05
 * @since
 */
public interface AddressMapper extends BaseMapper<Address> {

    List<AddressListVO> findlist(Integer levelType);
}
