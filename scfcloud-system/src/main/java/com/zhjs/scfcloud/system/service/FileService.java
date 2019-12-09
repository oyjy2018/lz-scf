package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.FileUploadLog;
import com.zhjs.scfcloud.model.common.Result;

import java.util.List;

/**
 * 文件信息管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 13:46
 *
 * @author liuchanghai
 * @create 2019-06-13 13:46
 * @since
 */
public interface FileService extends IService<File> {

    /**
     * 添加文件
     * @param file
     * @return
     */
    File add(File file);

    /**
     * 根据条件选择的查询文件
     * @param dto
     * @return
     */
    Result<List<File>> findSelective(FileDTO dto);

    /**
     * 查询文件
     * 筛选层级 companyId > businessId > businessType > fileSpecies
     * companyId 不能为空
     * businessId 不能为空
     * @param companyId 入参 公司ID
     * @param businessId 入参 业务ID
     * @param businessTypeId 入参 业务类型
     * @return
     */
    List<File> findList(Long companyId, Long businessId, Long businessTypeId);

    /**
     *  根据 fileUrl 查询文件
     * @param fileUrl 入参
     * @return
     */
    File findByFileUrl(String fileUrl);

    /**
     * 保存文件上传日记
     * @param fileUploadLog
     * @return
     */
    boolean addFileUploadLog(FileUploadLog fileUploadLog);

    /**
     *  根据 fileUrl 查询文件日志
     * @param fileUrl 入参
     * @return
     */
    FileUploadLog findLogByFileUrl(String fileUrl);
    /**
     * 文件批量保存
     * @param files
     * @return
     */
    boolean addBatch(List<File> files);

    /**
     * 查询垃圾文件
     * @return
     */
    List<FileUploadLog> findRubbishFileList();

    /**
     * 清除垃圾文件
     * @return
     */
    boolean deleteRubbishFile(List<String> fileUrlList);

    /**
     * 根据文件CODE集合查询
     * @param fileCodes
     * @return
     */
    List<File> findByFileCodes(String fileCodes);
}
