package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.business.AttrValDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import com.zhjs.scfcloud.model.vo.business.AttrValVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 业务字段属性值管理数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 15:22
 *
 * @author liuchanghai
 * @create 2019-06-12 15:22
 * @since
 */

@Mapper(componentModel = "spring")
public interface BusinessAttrValTransfer {

    List<AttrValVO> Po2AttrVO(List<BusinessAttrVal> list);

    BusinessAttrVal attrValDTO2Po(AttrValDTO dto);
}
