package com.linln.admin.order.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author 小王子
 * @date 2020/01/17
 */
@Data
public class SysVisitorValid implements Serializable {
    @NotEmpty(message = "标题不能为空")
    private String title;
}