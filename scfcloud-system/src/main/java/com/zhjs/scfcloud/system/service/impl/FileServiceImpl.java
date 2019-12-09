package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.entity.FileUploadLog;
import com.zhjs.scfcloud.model.mapper.FileMapper;
import com.zhjs.scfcloud.model.mapper.FileUploadLogMapper;
import com.zhjs.scfcloud.system.controller.FileController;
import com.zhjs.scfcloud.system.service.FileService;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件信息管理的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 13:47
 *
 * @author liuchanghai
 * @create 2019-06-13 13:47
 * @since
 */

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileUploadLogMapper fileUploadLogMapper;

    /**
     * 保存文件
     * @param file 入参
     * @return
     */
    @Override
    public File add(File file) {
        // 保存文件
        save(file);
        return file;
    }

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
    @Override
    public List<File> findList(Long companyId, Long businessId, Long businessTypeId) {
        log.info("查询文件 findList");
        if (companyId == null && businessId == null){
            return null;
        }
        LambdaQueryWrapper<File> lambda = new QueryWrapper<File>().lambda();
        if (companyId != null){
            lambda.eq(File::getCompanyId,companyId);
        }
        if (businessId != null){
            lambda.eq(File::getBusinessId,businessId);
        }
        if (businessTypeId != null){
            lambda.eq(File::getBusinessTypeId,businessTypeId);
        }
        lambda.orderByDesc(File::getId);
        return list(lambda);
    }

    /**
     * 根据条件查询文件
     * @param dto 入参
     * @return
     */
    @Override
    public Result<List<File>> findSelective(FileDTO dto) {
        if (dto.getId() != null){
            return Result.success(getById(dto.getId()));
        }
        LambdaQueryWrapper<File> lambda = new QueryWrapper<File>().lambda();
        if (dto.getCompanyId() != null){
            lambda.eq(File::getCompanyId,dto.getCompanyId());
        }
        if (dto.getBusinessId() != null){
            lambda.eq(File::getBusinessId,dto.getBusinessId());
        }
        if (dto.getBusinessTypeId() != null){
            lambda.eq(File::getBusinessTypeId,dto.getBusinessTypeId());
        }
        if (!StringUtil.isEmpty(dto.getNewFileName())){
            lambda.eq(File::getNewFileName,dto.getNewFileName());
        }
        if (!StringUtil.isEmpty(dto.getFileCode())){
            lambda.eq(File::getFileCode,dto.getFileCode());
        }
        if (!StringUtil.isEmpty(dto.getFileUrl())){
            lambda.eq(File::getFileUrl,dto.getFileUrl());
        }
        lambda.orderByDesc(File::getId);
        return Result.success(list(lambda));
    }

    /**
     * 根据 fileUrl 查询文件
     * @param fileUrl 入参
     * @return
     */
    @Override
    public File findByFileUrl(String fileUrl) {
        LambdaQueryWrapper<File> lambda = new QueryWrapper<File>().lambda();
        lambda.eq(File::getFileUrl,fileUrl);
        return getOne(lambda);
    }

    /**
     * 添加文件上传日志
     * @param fileUploadLog
     * @return
     */
    @Override
    public boolean addFileUploadLog(FileUploadLog fileUploadLog) {
        int insert = fileUploadLogMapper.insert(fileUploadLog);
        return insert == 1 ? true:false;
    }

    @Override
    public FileUploadLog findLogByFileUrl(String fileUrl) {
        LambdaQueryWrapper<FileUploadLog> lambda = new QueryWrapper<FileUploadLog>().lambda();
        lambda.eq(FileUploadLog::getFileUrl,fileUrl);
        return fileUploadLogMapper.selectOne(lambda);
    }

    /**
     * 批量添加文件
     * @param files
     * @return
     */
    @Override
    public boolean addBatch(List<File> files) {
        log.info("批量添加文件{}" + files.toString());
        return saveBatch(files);
    }

    /**
     * 查询垃圾文件
     * @return
     */
    @Override
    public List<FileUploadLog> findRubbishFileList() {
        return fileUploadLogMapper.findRubbishFileList();
    }

    /**
     * 删除垃圾文件
     * @param fileUrlList
     * @return
     */
    @Override
    public boolean deleteRubbishFile(List<String> fileUrlList) {
        // 删除文件
        fileUrlList.forEach(fileUrl ->{
            log.info("删除垃圾文件{}" + fileUrl);
            try {
                Files.delete(Paths.get(fileUrl));
            } catch (IOException e) {
                e.printStackTrace();
                log.info("删除垃圾文件异常{}" + e.getMessage());
            }
        });
        // 删除数据库记录
        LambdaQueryWrapper<FileUploadLog> lambda = new QueryWrapper<FileUploadLog>().lambda();
        lambda.eq(FileUploadLog::getFileUrl,fileUrlList);
        int delete = fileUploadLogMapper.delete(lambda);
        log.info("删除垃圾文件数据库记录{}");
        return delete >= 0 ? true:false;
    }

    @Override
    public List<File> findByFileCodes(String fileCodes) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.apply("find_in_set(file_code,{0})","'"+fileCodes+"'");
        return list(wrapper);
    }
}
