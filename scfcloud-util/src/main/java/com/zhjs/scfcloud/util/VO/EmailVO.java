package com.zhjs.scfcloud.util.VO;

import lombok.Data;

import java.util.Map;

@Data
public class EmailVO {
    //收件人列表
    private String[] to;
    //标题
    private String title;
    //邮件模板
    private String template;
    //模板参数列表
    private Map<String, Object> variables;
}
