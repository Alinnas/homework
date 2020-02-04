package com.linln.admin.menjinhbei.controller;

import com.linln.admin.menjinhbei.domain.Menjinhbei;
import com.linln.admin.menjinhbei.service.MenjinhbeiService;
import com.linln.admin.menjinhbei.validator.MenjinhbeiValid;
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
@RequestMapping("/menjinhbei")
public class MenjinhbeiController {

    @Autowired
    private MenjinhbeiService menjinhbeiService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("menjinhbei:index")
    public String index(Model model, Menjinhbei menjinhbei) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Menjinhbei> example = Example.of(menjinhbei, matcher);
        Page<Menjinhbei> list = menjinhbeiService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/menjinhbei/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("menjinhbei:add")
    public String toAdd() {
        return "/menjinhbei/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("menjinhbei:edit")
    public String toEdit(@PathVariable("id") Menjinhbei menjinhbei, Model model) {
        model.addAttribute("menjinhbei", menjinhbei);
        return "/menjinhbei/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"menjinhbei:add", "menjinhbei:edit"})
    @ResponseBody
    public ResultVo save(@Validated MenjinhbeiValid valid, Menjinhbei menjinhbei) {
        // 复制保留无需修改的数据
        if (menjinhbei.getId() != null) {
            Menjinhbei beMenjinhbei = menjinhbeiService.getById(menjinhbei.getId());
            EntityBeanUtil.copyProperties(beMenjinhbei, menjinhbei);
        }

        // 保存数据
        menjinhbeiService.save(menjinhbei);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("menjinhbei:detail")
    public String toDetail(@PathVariable("id") Menjinhbei menjinhbei, Model model) {
        model.addAttribute("menjinhbei",menjinhbei);
        return "/menjinhbei/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("menjinhbei:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (menjinhbeiService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}