package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.entity.CreditRecord;
import com.zhjs.scfcloud.model.vo.credit.CreditRecordAllListWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditRecordMyListAppVO;
import com.zhjs.scfcloud.model.vo.credit.CreditRecordMyListWebVO;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-08-26 17:36
 */
public interface CreditRecordMapper extends BaseMapper<CreditRecord> {
    List<CreditRecordMyListAppVO> getMyListAppVOList(CreditRecordMyListQueryDTO creditRecordMyListQueryDTO);

    List<CreditRecordMyListWebVO> getMyListWebVOList(CreditRecordMyListQueryDTO creditRecordMyListQueryDTO);

    List<CreditRecordAllListWebVO> getAllListVOList(CreditRecordAllListQueryDTO creditRecordAllListQueryDTO);

    /**
     * 根据身份证查询授信记录
     * @param idCard
     * @return
     */
    List<PersonalCreditVO> findPersonalCreditList(@Param("idCard") String idCard, @Param("companyId") Long companyId);

    /**
     * 根据授信记录ID查询
     * @param creditRecordId
     * @return
     */
    PersonalCreditVO findPersonalCredit(Long creditRecordId);

    /**
     * 查询授信交叉的数量
     * @param projectName
     * @param relateProjectId
     * @param creditStartDate
     * @param creditEndDate
     * @param companyId
     * @return
     */
    int getMixedRecordCount(@Param("projectName") String projectName, @Param("relateProjectId") String relateProjectId, @Param("creditStartDate") String creditStartDate, @Param("creditEndDate") String creditEndDate, @Param("companyId")Long companyId);

}