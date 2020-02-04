package com.linln.admin.order.controller;

import com.linln.admin.order.domain.SysVisitor;
import com.linln.admin.order.service.SysVisitorService;
import com.linln.admin.order.validator.SysVisitorValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
@Controller
@RequestMapping("/sysVisitor")
public class SysVisitorController {

    @Autowired
    private SysVisitorService sysVisitorService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("sysVisitor:index")
    public String index(Model model, SysVisitor sysVisitor) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("endDate", match -> match.contains())
                .withMatcher("name", match -> match.contains())
                .withMatcher("createDate", match -> match.contains());

        // 获取数据列表
        Example<SysVisitor> example = Example.of(sysVisitor, matcher);
        Page<SysVisitor> list = sysVisitorService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/sysVisitor/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("sysVisitor:add")
    public String toAdd() {
        return "/sysVisitor/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("sysVisitor:edit")
    public String toEdit(@PathVariable("id") SysVisitor sysVisitor, Model model) {
        model.addAttribute("sysVisitor", sysVisitor);
        return "/sysVisitor/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"sysVisitor:add", "sysVisitor:edit"})
    @ResponseBody
    public ResultVo save(@Validated SysVisitorValid valid, SysVisitor sysVisitor) {
        // 复制保留无需修改的数据
        if (sysVisitor.getId() != null) {
            SysVisitor beSysVisitor = sysVisitorService.getById(sysVisitor.getId());
            EntityBeanUtil.copyProperties(beSysVisitor, sysVisitor);
        }

        // 保存数据
        sysVisitorService.save(sysVisitor);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("sysVisitor:detail")
    public String toDetail(@PathVariable("id") SysVisitor sysVisitor, Model model) {
        model.addAttribute("sysVisitor",sysVisitor);
        return "/sysVisitor/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("sysVisitor:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (sysVisitorService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}