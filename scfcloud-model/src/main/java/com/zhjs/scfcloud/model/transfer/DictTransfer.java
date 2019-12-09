package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddDictDTO;
import com.zhjs.scfcloud.model.dto.AddNatureDTO;
import com.zhjs.scfcloud.model.dto.EditDictDTO;
import com.zhjs.scfcloud.model.entity.Dictionary;
import com.zhjs.scfcloud.model.entity.DictionaryDetails;
import com.zhjs.scfcloud.model.vo.DictKeyValueListVO;
import com.zhjs.scfcloud.model.vo.DictVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 字典数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 17:37
 *
 * @author liuchanghai
 * @create 2019-05-22 17:37
 * @since
 */

@Mapper(componentModel = "spring")
public interface DictTransfer {

    Dictionary addDto2Po(AddDictDTO dto);

    Dictionary editDto2Po(EditDictDTO dto);

    List<DictVO> dicts2DictVo(List<Dictionary> list);

    List<DictKeyValueListVO> dictDetails2keyValueListVo(List<DictionaryDetails> list);

    DictionaryDetails addNatureDto2Po(AddNatureDTO dto);
}
