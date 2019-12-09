package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddProductDTO;
import com.zhjs.scfcloud.model.dto.EditProductDTO;
import com.zhjs.scfcloud.model.entity.Product;
import com.zhjs.scfcloud.model.vo.ProductListVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 产品数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 11:39
 *
 * @author liuchanghai
 * @create 2019-05-27 11:39
 * @since
 */

@Mapper(componentModel = "spring")
public interface ProductTransfer {

    Product addDTO2Po(AddProductDTO dto);

    Product editDTO2Po(EditProductDTO dto);

    List<ProductListVO> products2ProductListVO(List<Product> list);
}
