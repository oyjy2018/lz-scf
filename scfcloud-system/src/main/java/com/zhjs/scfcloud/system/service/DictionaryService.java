package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Dictionary;
import com.zhjs.scfcloud.model.vo.DictKeyValueListVO;
import com.zhjs.scfcloud.model.vo.DictVO;

import java.util.List;

/**
 * 系统数据字典业务逻辑接口
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 15:59
 * @since
 */
public interface DictionaryService extends IService<Dictionary> {

    boolean add(AddDictDTO dto);

    boolean isExist(IsExistDictDTO dto);

    boolean edit(EditDictDTO dto);

    List<DictVO> findList(DictListDTO dto);

    List<DictKeyValueListVO> natureSelectList(NatureSelectListDTO dto);

    List<DictKeyValueListVO> staffSizeSelectList(StaffSizeSelectListDTO dto);

    boolean addNature(AddNatureDTO dto);
}
