package com.linln.admin.emp.controller;

import com.linln.admin.emp.domain.Emp;
import com.linln.admin.emp.service.EmpService;
import com.linln.admin.emp.validator.EmpValid;
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
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("emp:index")
    public String index(Model model, Emp emp) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Emp> example = Example.of(emp, matcher);
        Page<Emp> list = empService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/emp/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("emp:add")
    public String toAdd() {
        return "/emp/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("emp:edit")
    public String toEdit(@PathVariable("id") Emp emp, Model model) {
        model.addAttribute("emp", emp);
        return "/emp/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"emp:add", "emp:edit"})
    @ResponseBody
    public ResultVo save(@Validated EmpValid valid, Emp emp) {
        // 复制保留无需修改的数据
        if (emp.getId() != null) {
            Emp beEmp = empService.getById(emp.getId());
            EntityBeanUtil.copyProperties(beEmp, emp);
        }

        // 保存数据
        empService.save(emp);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("emp:detail")
    public String toDetail(@PathVariable("id") Emp emp, Model model) {
        model.addAttribute("emp",emp);
        return "/emp/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("emp:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (empService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}