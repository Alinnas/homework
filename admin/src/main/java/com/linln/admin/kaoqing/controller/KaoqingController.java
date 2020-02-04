package com.linln.admin.kaoqing.controller;

import com.linln.admin.kaoqing.domain.Kaoqing;
import com.linln.admin.kaoqing.service.KaoqingService;
import com.linln.admin.kaoqing.validator.KaoqingValid;
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
@RequestMapping("/kaoqing")
public class KaoqingController {

    @Autowired
    private KaoqingService kaoqingService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("kaoqing:index")
    public String index(Model model, Kaoqing kaoqing) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Kaoqing> example = Example.of(kaoqing, matcher);
        Page<Kaoqing> list = kaoqingService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/kaoqing/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("kaoqing:add")
    public String toAdd() {
        return "/kaoqing/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("kaoqing:edit")
    public String toEdit(@PathVariable("id") Kaoqing kaoqing, Model model) {
        model.addAttribute("kaoqing", kaoqing);
        return "/kaoqing/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"kaoqing:add", "kaoqing:edit"})
    @ResponseBody
    public ResultVo save(@Validated KaoqingValid valid, Kaoqing kaoqing) {
        // 复制保留无需修改的数据
        if (kaoqing.getId() != null) {
            Kaoqing beKaoqing = kaoqingService.getById(kaoqing.getId());
            EntityBeanUtil.copyProperties(beKaoqing, kaoqing);
        }

        // 保存数据
        kaoqingService.save(kaoqing);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("kaoqing:detail")
    public String toDetail(@PathVariable("id") Kaoqing kaoqing, Model model) {
        model.addAttribute("kaoqing",kaoqing);
        return "/kaoqing/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("kaoqing:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (kaoqingService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}