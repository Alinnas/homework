package com.linln.admin.rilisetting.controller;

import com.linln.admin.rilisetting.domain.Rilisetting;
import com.linln.admin.rilisetting.service.RilisettingService;
import com.linln.admin.rilisetting.validator.RilisettingValid;
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
@RequestMapping("/rilisetting/rilisetting")
public class RilisettingController {

    @Autowired
    private RilisettingService rilisettingService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("rilisetting:rilisetting:index")
    public String index(Model model, Rilisetting rilisetting) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Rilisetting> example = Example.of(rilisetting, matcher);
        Page<Rilisetting> list = rilisettingService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/rilisetting/rilisetting/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("rilisetting:rilisetting:add")
    public String toAdd() {
        return "/rilisetting/rilisetting/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("rilisetting:rilisetting:edit")
    public String toEdit(@PathVariable("id") Rilisetting rilisetting, Model model) {
        model.addAttribute("rilisetting", rilisetting);
        return "/rilisetting/rilisetting/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"rilisetting:rilisetting:add", "rilisetting:rilisetting:edit"})
    @ResponseBody
    public ResultVo save(@Validated RilisettingValid valid, Rilisetting rilisetting) {
        // 复制保留无需修改的数据
        if (rilisetting.getId() != null) {
            Rilisetting beRilisetting = rilisettingService.getById(rilisetting.getId());
            EntityBeanUtil.copyProperties(beRilisetting, rilisetting);
        }

        // 保存数据
        rilisettingService.save(rilisetting);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("rilisetting:rilisetting:detail")
    public String toDetail(@PathVariable("id") Rilisetting rilisetting, Model model) {
        model.addAttribute("rilisetting",rilisetting);
        return "/rilisetting/rilisetting/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("rilisetting:rilisetting:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (rilisettingService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}