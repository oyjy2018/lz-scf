package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.CreditRecord;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;

import java.util.List;

/**
 * 业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:46
 *
 * @author liuchanghai
 * @create 2019-06-11 15:46
 * @since
 */
public interface CreditRecordService extends IService<CreditRecord> {

    String myList(CreditRecordMyListQueryDTO creditRecordMyListQueryDTO);

    String allList(CreditRecordAllListQueryDTO creditRecordAllListQueryDTO);

    /**
     * 根据身份证查询授信记录
     * @param idCard
     * @return
     */
    List<PersonalCreditVO> findPersonalCreditList(String idCard, Long companyId);

    /**
     * 根据授信记录ID查询
     * @param creditRecordId
     * @return
     */
    PersonalCreditVO findPersonalCredit(Long creditRecordId);

    /**
     * 导入授信
     * @param dto
     * @return
     */
    Result importRecord(CreditRecordImportDTO dto);

    /**
     * 删除授信
     * @param dto
     * @return
     */
    Result deleteRecord(BaseDeleteDTO dto);

    /**
     * 获取项目授信余额
     * @param dto
     * @return
     */
    Result findCreditBalance(CreditRecordBalanceQueryDTO dto);

    /**
     * 查询授余额-内部
     * @param id
     * @return
     */
    Result findBalance(Long id);
}
