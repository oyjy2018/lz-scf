package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Dictionary;
import com.zhjs.scfcloud.model.entity.DictionaryDetails;
import com.zhjs.scfcloud.model.mapper.DictionaryMapper;
import com.zhjs.scfcloud.model.transfer.DictTransfer;
import com.zhjs.scfcloud.model.vo.DictKeyValueListVO;
import com.zhjs.scfcloud.model.vo.DictVO;
import com.zhjs.scfcloud.system.service.DictionaryDetailsService;
import com.zhjs.scfcloud.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统数据字典业务逻辑接口实现类
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 16:00
 * @since
 */

@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Autowired
    private DictionaryDetailsService dictionaryDetailsService;
    @Autowired
    private DictTransfer dictTransfer;

    @Override
    public boolean add(AddDictDTO dto) {
       Dictionary dict = dictTransfer.addDto2Po(dto);
        return save(dict);
    }

    @Override
    public boolean isExist(IsExistDictDTO dto) {
        return false;
    }

    @Override
    public boolean edit(EditDictDTO dto) {
        Dictionary dict = dictTransfer.editDto2Po(dto);
        return updateById(dict);
    }

    @Override
    public List<DictVO> findList(DictListDTO dto) {
        List<Dictionary> list = list(null);
        List<DictVO> result = dictTransfer.dicts2DictVo(list);
        return result;
    }

    @Override
    public List<DictKeyValueListVO> natureSelectList(NatureSelectListDTO dto) {
        QueryWrapper<DictionaryDetails> where;
        if(dto.getKey() != null && !dto.getKey().isEmpty()){
            where = new QueryWrapper<DictionaryDetails>()
                    .eq("parent_key","concrete")
                    .eq("remark1",dto.getKey());
        }else {
            where = new QueryWrapper<DictionaryDetails>()
                    .eq("parent_key", "nature");
        }
        List<DictionaryDetails> list = dictionaryDetailsService.list(where);
        List<DictKeyValueListVO> result = dictTransfer.dictDetails2keyValueListVo(list);
        return result;
    }

    @Override
    public List<DictKeyValueListVO> staffSizeSelectList(StaffSizeSelectListDTO dto) {
        QueryWrapper<DictionaryDetails> where = new QueryWrapper<DictionaryDetails>()
                    .eq("parent_key", "staff_size");
        List<DictionaryDetails> list = dictionaryDetailsService.list(where);
        List<DictKeyValueListVO> result = dictTransfer.dictDetails2keyValueListVo(list);
        return  result;
    }

    @Override
    public boolean addNature(AddNatureDTO dto) {
        DictionaryDetails dict = dictTransfer.addNatureDto2Po(dto);
        return dictionaryDetailsService.save(dict);
    }
}
