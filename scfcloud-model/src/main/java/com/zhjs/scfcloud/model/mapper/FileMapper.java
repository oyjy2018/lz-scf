package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件库 Mapper 接口
 * @author:dailongting
 * @date:2019-06-20 11:53
 */
public interface FileMapper extends BaseMapper<File> {
    int deleteByPrimaryKey(Long id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

    /**
     * 根据文件CODE集合查询文件
     * @param companyId
     * @param businessTypeId
     * @param businessId
     * @param fileCodes
     * @return
     */
    List<File> findFileByFileCodes(@Param("companyId")Long companyId,
                                   @Param("businessTypeId")Long businessTypeId,
                                   @Param("businessId")Long businessId,
                                   @Param("fileCodes")String fileCodes);
}