package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddDeptDTO;
import com.zhjs.scfcloud.model.dto.EditDeptDTO;
import com.zhjs.scfcloud.model.entity.Department;
import com.zhjs.scfcloud.model.vo.DeptSelectVO;
import com.zhjs.scfcloud.model.vo.DeptVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 部门数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 14:18
 *
 * @author liuchanghai
 * @create 2019-05-20 14:18
 * @since
 */

@Mapper(componentModel = "spring")
public interface DeptTransfer {

    Department addDTO2Po(AddDeptDTO dto);

    DeptVO po2DeptVo(Department po);

    List<DeptVO> depts2DeptVoList(List<Department> list);

    Department editDeptDto2Po(EditDeptDTO dto);

    List<DeptSelectVO> depts2DeptSelectVO(List<Department> list);
}
