package com.linln.admin.kaoqinsetting.controller;

import com.linln.admin.kaoqinsetting.domain.Kaoqinsetting;
import com.linln.admin.kaoqinsetting.service.KaoqinsettingService;
import com.linln.admin.kaoqinsetting.validator.KaoqinsettingValid;
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
@RequestMapping("/kaoqinsetting/kaoqinsetting")
public class KaoqinsettingController {

    @Autowired
    private KaoqinsettingService kaoqinsettingService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("kaoqinsetting:kaoqinsetting:index")
    public String index(Model model, Kaoqinsetting kaoqinsetting) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Kaoqinsetting> example = Example.of(kaoqinsetting, matcher);
        Page<Kaoqinsetting> list = kaoqinsettingService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/kaoqinsetting/kaoqinsetting/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("kaoqinsetting:kaoqinsetting:add")
    public String toAdd() {
        return "/kaoqinsetting/kaoqinsetting/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("kaoqinsetting:kaoqinsetting:edit")
    public String toEdit(@PathVariable("id") Kaoqinsetting kaoqinsetting, Model model) {
        model.addAttribute("kaoqinsetting", kaoqinsetting);
        return "/kaoqinsetting/kaoqinsetting/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"kaoqinsetting:kaoqinsetting:add", "kaoqinsetting:kaoqinsetting:edit"})
    @ResponseBody
    public ResultVo save(@Validated KaoqinsettingValid valid, Kaoqinsetting kaoqinsetting) {
        // 复制保留无需修改的数据
        if (kaoqinsetting.getId() != null) {
            Kaoqinsetting beKaoqinsetting = kaoqinsettingService.getById(kaoqinsetting.getId());
            EntityBeanUtil.copyProperties(beKaoqinsetting, kaoqinsetting);
        }

        // 保存数据
        kaoqinsettingService.save(kaoqinsetting);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("kaoqinsetting:kaoqinsetting:detail")
    public String toDetail(@PathVariable("id") Kaoqinsetting kaoqinsetting, Model model) {
        model.addAttribute("kaoqinsetting",kaoqinsetting);
        return "/kaoqinsetting/kaoqinsetting/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("kaoqinsetting:kaoqinsetting:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (kaoqinsettingService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}