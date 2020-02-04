package com.linln.admin.menjing.controller;

import com.linln.admin.menjing.domain.Menjing;
import com.linln.admin.menjing.service.MenjingService;
import com.linln.admin.menjing.validator.MenjingValid;
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
@RequestMapping("/menjing")
public class MenjingController {

    @Autowired
    private MenjingService menjingService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("menjing:index")
    public String index(Model model, Menjing menjing) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Menjing> example = Example.of(menjing, matcher);
        Page<Menjing> list = menjingService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/menjing/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("menjing:add")
    public String toAdd() {
        return "/menjing/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("menjing:edit")
    public String toEdit(@PathVariable("id") Menjing menjing, Model model) {
        model.addAttribute("menjing", menjing);
        return "/menjing/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"menjing:add", "menjing:edit"})
    @ResponseBody
    public ResultVo save(@Validated MenjingValid valid, Menjing menjing) {
        // 复制保留无需修改的数据
        if (menjing.getId() != null) {
            Menjing beMenjing = menjingService.getById(menjing.getId());
            EntityBeanUtil.copyProperties(beMenjing, menjing);
        }

        // 保存数据
        menjingService.save(menjing);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("menjing:detail")
    public String toDetail(@PathVariable("id") Menjing menjing, Model model) {
        model.addAttribute("menjing",menjing);
        return "/menjing/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("menjing:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (menjingService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}