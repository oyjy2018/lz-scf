package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.AddProductDTO;
import com.zhjs.scfcloud.model.dto.EditProductDTO;
import com.zhjs.scfcloud.model.dto.IsExistProductDTO;
import com.zhjs.scfcloud.model.dto.ProductListDTO;
import com.zhjs.scfcloud.model.entity.Product;
import com.zhjs.scfcloud.model.vo.ProductListVO;

import java.util.List;

/**
 * 产品管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 11:25
 *
 * @author liuchanghai
 * @create 2019-05-27 11:25
 * @since
 */

public interface ProductService extends IService<Product> {

    List<ProductListVO> findList(ProductListDTO dto);

    boolean edit(EditProductDTO dto);

    boolean isExist(IsExistProductDTO dto);

    boolean add(AddProductDTO dto);
}
