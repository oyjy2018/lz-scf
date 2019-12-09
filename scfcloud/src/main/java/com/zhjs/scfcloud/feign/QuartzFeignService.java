package com.zhjs.scfcloud.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/5/21 16:24
 */
@FeignClient(value = "scfcloud-task", contextId = "quartz")
public interface QuartzFeignService {

    @PostMapping("/quartz/jobList")
    Result jobList(@RequestBody Page page);

    @PostMapping("/quartz/pauseJob")
    Result pauseJob(@RequestParam Integer id);

    @PostMapping("/quartz/pauseAllJob")
    Result pauseAllJob();

    @PostMapping("/quartz/startJob")
    Result startJob(@RequestParam Integer id);

    @PostMapping("/quartz/startAllJob")
    Result startAllJob();

    @PostMapping("/quartz/updateCron")
    Result updateCron(@RequestParam Integer id, @RequestParam String cron);
}
