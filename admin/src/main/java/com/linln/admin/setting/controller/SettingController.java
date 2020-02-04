package com.linln.admin.setting.controller;

import com.linln.admin.setting.domain.Setting;
import com.linln.admin.setting.service.SettingService;
import com.linln.admin.setting.validator.SettingValid;
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
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("setting:index")
    public String index(Model model, Setting setting) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Setting> example = Example.of(setting, matcher);
        Page<Setting> list = settingService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/setting/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("setting:add")
    public String toAdd() {
        return "/setting/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("setting:edit")
    public String toEdit(@PathVariable("id") Setting setting, Model model) {
        model.addAttribute("setting", setting);
        return "/setting/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"setting:add", "setting:edit"})
    @ResponseBody
    public ResultVo save(@Validated SettingValid valid, Setting setting) {
        // 复制保留无需修改的数据
        if (setting.getId() != null) {
            Setting beSetting = settingService.getById(setting.getId());
            EntityBeanUtil.copyProperties(beSetting, setting);
        }

        // 保存数据
        settingService.save(setting);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("setting:detail")
    public String toDetail(@PathVariable("id") Setting setting, Model model) {
        model.addAttribute("setting",setting);
        return "/setting/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("setting:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (settingService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}