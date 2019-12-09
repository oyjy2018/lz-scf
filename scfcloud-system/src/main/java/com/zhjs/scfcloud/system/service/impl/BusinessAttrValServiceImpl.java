package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.AttrValDTO;
import com.zhjs.scfcloud.model.dto.business.QueryAttrValDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import com.zhjs.scfcloud.model.mapper.BusinessAttrValMapper;
import com.zhjs.scfcloud.model.transfer.BusinessAttrValTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.AttrValVO;
import com.zhjs.scfcloud.system.service.BusinessAttrValService;
import com.zhjs.scfcloud.util.constant.RedisConstant;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务字段属性值管理的业务逻辑接口的实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 14:17
 *
 * @author liuchanghai
 * @create 2019-06-12 14:17
 * @since
 */

@Service
public class BusinessAttrValServiceImpl extends ServiceImpl<BusinessAttrValMapper, BusinessAttrVal> implements BusinessAttrValService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessAttrValTransfer businessAttrValTransfer;

    /**
     * 根据 业务类型ID 或 业务属性ID 查询业务字段属性值
     * @param dto 入参
     * @return
     */
    @Override
    public Result findByTypeIdOrAttrId(QueryAttrValDTO dto) {
        logger.info("查询业务字段属性值{ }: " +dto.toString());
        // 公司类型ID为null 则返回 null
        if (dto.getCompanyId() == null){
            return Result.failure("公司类型ID不能为空");
        }
        // 从redis 中取数据
        List<BusinessAttrVal> attrValList = BusinessCfgUtil.getBusinessAttrValList(dto.getCompanyId(),
                dto.getBusinessTypeId(), dto.getBusinessAttrId(), null);
        if(!StringUtil.isEmpty(dto.getSuperiorAttrKey())){
            attrValList = attrValList.stream()
                    .filter(attrVal -> attrVal.getSuperiorAttrKey().equals(dto.getSuperiorAttrKey()))
                    .collect(Collectors.toList());
        }
        if (attrValList.size() > 0){
            logger.info("从redis中获取{ }: " +dto.toString());
            return Result.success(attrValList);
        }
        // 设置查询条件
        LambdaQueryWrapper<BusinessAttrVal> lambda = new QueryWrapper<BusinessAttrVal>().lambda();
        if(dto.getCompanyId() != null){
            lambda.eq(BusinessAttrVal::getCompanyId, dto.getCompanyId());
        }
        if(dto.getBusinessTypeId() != null){
            lambda.eq(BusinessAttrVal::getBusinessTypeId, dto.getBusinessTypeId());
        }
        if(dto.getBusinessAttrId() != null){
            lambda.eq(BusinessAttrVal::getBusinessAttrId, dto.getBusinessAttrId());
        }
        if(!StringUtil.isEmpty(dto.getSuperiorAttrKey())){
            lambda.eq(BusinessAttrVal::getSuperiorAttrKey, dto.getSuperiorAttrKey());
        }
        // 排序
        lambda.orderByAsc(BusinessAttrVal::getSort);
        logger.info("从数据库中获取{ }: " +dto.toString());
        List<BusinessAttrVal> list = list(lambda);
        if (list.size() > 0){
            // 数据转换
            List<AttrValVO> results = businessAttrValTransfer.Po2AttrVO(list);
            return Result.success(results);
        }
        return Result.success(list);
    }

    /**
     * 新建
     * @param dto 入参
     * @return
     */
    @Override
    public Result add(AttrValDTO dto) {
        BusinessAttrVal attrVal = businessAttrValTransfer.attrValDTO2Po(dto);
        attrVal.setCreateTime(new Date()).setUpdateTime(new Date());
        boolean result = save(attrVal);
        if (result){
            // 清空redis缓存
            String key = RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL + attrVal.getBusinessTypeId();
//            redisUtil.del(key);
            return Result.success("添加成功");
        }
        return Result.failure("添加失败");
    }
}
