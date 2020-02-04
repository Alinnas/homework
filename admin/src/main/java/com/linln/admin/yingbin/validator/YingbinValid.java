package com.linln.admin.yingbin.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

/**
 * @author 小王子
 * @date 2020/01/20
 */
@Data
public class YingbinValid implements Serializable {
    @NotEmpty(message = "标题不能为空")
    private String title;
}