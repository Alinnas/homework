package com.linln.admin.tongji.controller;

import com.linln.admin.tongji.domain.Tongji;
import com.linln.admin.tongji.service.TongjiService;
import com.linln.admin.tongji.validator.TongjiValid;
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
 * @date 2020/01/20
 */
@Controller
@RequestMapping("/tongji/tongji")
public class TongjiController {

    @Autowired
    private TongjiService tongjiService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("tongji:tongji:index")
    public String index(Model model, Tongji tongji) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Tongji> example = Example.of(tongji, matcher);
        Page<Tongji> list = tongjiService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/tongji/tongji/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("tongji:tongji:add")
    public String toAdd() {
        return "/tongji/tongji/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("tongji:tongji:edit")
    public String toEdit(@PathVariable("id") Tongji tongji, Model model) {
        model.addAttribute("tongji", tongji);
        return "/tongji/tongji/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"tongji:tongji:add", "tongji:tongji:edit"})
    @ResponseBody
    public ResultVo save(@Validated TongjiValid valid, Tongji tongji) {
        // 复制保留无需修改的数据
        if (tongji.getId() != null) {
            Tongji beTongji = tongjiService.getById(tongji.getId());
            EntityBeanUtil.copyProperties(beTongji, tongji);
        }

        // 保存数据
        tongjiService.save(tongji);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("tongji:tongji:detail")
    public String toDetail(@PathVariable("id") Tongji tongji, Model model) {
        model.addAttribute("tongji",tongji);
        return "/tongji/tongji/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("tongji:tongji:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (tongjiService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}