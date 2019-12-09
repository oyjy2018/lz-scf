package com.zhjs.scfcloud.model.dto.ticket.acceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class AcceptorListDTO extends Page {
    private Long companyId;

    private String acceptorName;
}
