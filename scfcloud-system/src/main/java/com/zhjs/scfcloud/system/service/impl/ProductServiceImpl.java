package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.dto.AddProductDTO;
import com.zhjs.scfcloud.model.dto.EditProductDTO;
import com.zhjs.scfcloud.model.dto.IsExistProductDTO;
import com.zhjs.scfcloud.model.dto.ProductListDTO;
import com.zhjs.scfcloud.model.entity.Product;
import com.zhjs.scfcloud.model.mapper.ProductMapper;
import com.zhjs.scfcloud.model.transfer.ProductTransfer;
import com.zhjs.scfcloud.model.vo.ProductListVO;
import com.zhjs.scfcloud.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品管理业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 11:26
 *
 * @author liuchanghai
 * @create 2019-05-27 11:26
 * @since
 */

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductTransfer productTransfer;

    @Override
    public List<ProductListVO> findList(ProductListDTO dto) {
        List<Product> list = list(null);
        List<ProductListVO> result = productTransfer.products2ProductListVO(list);
        return result;
    }

    @Override
    public boolean edit(EditProductDTO dto) {
        Product product = productTransfer.editDTO2Po(dto);
        return updateById(product);
    }

    @Override
    public boolean isExist(IsExistProductDTO dto) {
        return false;
    }

    @Override
    public boolean add(AddProductDTO dto) {
        Product product = productTransfer.addDTO2Po(dto);
        return save(product);
    }
}
