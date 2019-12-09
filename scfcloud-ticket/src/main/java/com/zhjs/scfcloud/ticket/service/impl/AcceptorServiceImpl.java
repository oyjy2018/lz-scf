package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorAddDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorListDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorUpdateDTO;
import com.zhjs.scfcloud.model.entity.Acceptor;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.mapper.AcceptorMapper;
import com.zhjs.scfcloud.model.mapper.CompanyMapper;
import com.zhjs.scfcloud.model.transfer.AcceptorTransfer;
import com.zhjs.scfcloud.model.vo.credit.acceptor.AcceptorListVO;
import com.zhjs.scfcloud.ticket.service.AcceptorService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AcceptorServiceImpl extends ServiceImpl<AcceptorMapper, Acceptor> implements AcceptorService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AcceptorMapper acceptorMapper;
    @Resource
    private AcceptorTransfer acceptorTransfer;
    @Resource
    private CompanyMapper companyMapper;

    //查询承兑方列表
    @Override
    public Result findList(AcceptorListDTO dto) {
        dto.setRecords(acceptorMapper.findList(dto));
        return Result.successPage(dto);
    }

    //新增承兑方
    @Override
    public Result add(AcceptorAddDTO dto) {
        //名字处理
        String acceptorName = dto.getAcceptorName().trim();
        acceptorName = acceptorName.replace("(","（").replace(")","）").replace(" ","");
        dto.setAcceptorName(acceptorName);
        //名字重复校验
        LambdaQueryWrapper<Acceptor> wrapper = new  QueryWrapper<Acceptor>().lambda();
        wrapper.eq(Acceptor::getAcceptorName,dto.getAcceptorName());
        wrapper.ne(Acceptor::getDeleteStatus, CommonEnum.WhetherEnum.YES.getStatus());
        if (acceptorMapper.selectCount(wrapper) > 0) {
            return Result.failure("您输入的承兑方公司已存在于您的承兑方列表");
        }
        //转换
        Acceptor acceptor = acceptorTransfer.toAcceptor(dto);
        acceptor.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
        acceptor.setCreateBy(dto.getUserId());
        acceptor.setCreateTime(new Date());
        if (acceptorMapper.insert(acceptor) != 1) {
            return Result.failure("新增失败");
        }
        return Result.success("新增成功");
    }

    //删除承兑方
    @Override
    public Result delete(BaseDeleteDTO dto) {
        Acceptor acceptor = acceptorMapper.selectById(dto.getId());
        if (acceptor == null) return Result.failure("承兑方不存在");

        //逻辑删除
        acceptor.setDeleteStatus(CommonEnum.WhetherEnum.YES.getStatus());
        acceptor.setUpdateBy(dto.getUserId());
        acceptor.setUpdateTime(new Date());
        if (acceptorMapper.updateById(acceptor) != 1 ) {
            return Result.failure("删除失败");
        }
        return Result.success();
    }

    //修改承兑方
    @Override
    public Result update(AcceptorUpdateDTO dto) {
        Acceptor acceptor = acceptorMapper.selectById(dto.getId());
        if (acceptor == null) return Result.failure("承兑方不存在");

        acceptor.setLimitMoney(new BigDecimal(dto.getLimitMoney()));
        acceptor.setRemark(dto.getRemark());
        acceptor.setUpdateBy(dto.getUserId());
        acceptor.setUpdateTime(new Date());

        if (acceptorMapper.updateById(acceptor) != 1) {
            return Result.failure("修改失败");
        }

        return Result.success();
    }

    @Override
    public Result detail(Long id, Long companyId) {
        AcceptorListVO vo = acceptorMapper.getDetail(id,companyId);
        return Result.success(vo);
    }

    @Override
    public Result updateLimit(BaseSingleUpdateDTO dto) {
        Company company = companyMapper.selectById(dto.getId());
        if (company == null) return Result.failure("公司不存在");
        company.setUpdateById(dto.getUpdateBy());
        company.setUpdateTime(LocalDateTime.now());
        company.setIsAcceptorLimit(Integer.parseInt(dto.getValue()));
        if (companyMapper.updateById(company) != 1) {
            return Result.failure("更新失败");
        }
        return Result.success();
    }

    @Override
    public Result isLimit(Long companyId) {
        Company company = companyMapper.selectById(companyId);
        if (company == null) return Result.failure("公司不存在");

        return Result.successData(company.getIsAcceptorLimit());
    }
}
