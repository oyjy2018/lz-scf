package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.AttrValDTO;
import com.zhjs.scfcloud.model.dto.business.QueryAttrValDTO;
import com.zhjs.scfcloud.system.service.BusinessAttrValService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务字段属性值管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 15:46
 *
 * @author liuchanghai
 * @create 2019-06-12 15:46
 * @since
 */

@Api(tags = "业务字段属性值管理")
@RestController
@RequestMapping("/business/attr/val/")
public class BusinessAttrValController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessAttrValService businessAttrValService;

    /**
     * 查询业务字段属性值
     * @param dto 入参
     * @return
     */
    @PostMapping("findByTypeIdOrAttrId")
    public Result findByTypeIdOrAttrId(@RequestBody QueryAttrValDTO dto) {
        return businessAttrValService.findByTypeIdOrAttrId(dto);
    }

    /**
     * 新建业务字段属性值
     * @param dto 入参
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestBody AttrValDTO dto) {
        return businessAttrValService.add(dto);
    }
}
