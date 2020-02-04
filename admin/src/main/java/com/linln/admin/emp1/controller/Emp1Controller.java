package com.linln.admin.emp1.controller;

import com.linln.admin.emp1.domain.Emp1;
import com.linln.admin.emp1.service.Emp1Service;
import com.linln.admin.emp1.validator.Emp1Valid;
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
@RequestMapping("/emp1")
public class Emp1Controller {

    @Autowired
    private Emp1Service emp1Service;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("emp1:index")
    public String index(Model model, Emp1 emp1) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("dep_id", match -> match.contains())
                .withMatcher("name", match -> match.contains())
                .withMatcher("createDate", match -> match.contains())
                .withMatcher("endDate", match -> match.contains());

        // 获取数据列表
        Example<Emp1> example = Example.of(emp1, matcher);
        Page<Emp1> list = emp1Service.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/emp1/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("emp1:add")
    public String toAdd() {
        return "/emp1/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("emp1:edit")
    public String toEdit(@PathVariable("id") Emp1 emp1, Model model) {
        model.addAttribute("emp1", emp1);
        return "/emp1/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"emp1:add", "emp1:edit"})
    @ResponseBody
    public ResultVo save(@Validated Emp1Valid valid, Emp1 emp1) {
        // 复制保留无需修改的数据
        if (emp1.getId() != null) {
            Emp1 beEmp1 = emp1Service.getById(emp1.getId());
            EntityBeanUtil.copyProperties(beEmp1, emp1);
        }

        // 保存数据
        emp1Service.save(emp1);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("emp1:detail")
    public String toDetail(@PathVariable("id") Emp1 emp1, Model model) {
        model.addAttribute("emp1",emp1);
        return "/emp1/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("emp1:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (emp1Service.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}