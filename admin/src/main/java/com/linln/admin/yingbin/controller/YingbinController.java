package com.linln.admin.yingbin.controller;

import com.linln.admin.yingbin.domain.Yingbin;
import com.linln.admin.yingbin.service.YingbinService;
import com.linln.admin.yingbin.validator.YingbinValid;
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
@RequestMapping("/yingbin")
public class YingbinController {

    @Autowired
    private YingbinService yingbinService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("yingbin:index")
    public String index(Model model, Yingbin yingbin) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Yingbin> example = Example.of(yingbin, matcher);
        Page<Yingbin> list = yingbinService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/yingbin/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("yingbin:add")
    public String toAdd() {
        return "/yingbin/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("yingbin:edit")
    public String toEdit(@PathVariable("id") Yingbin yingbin, Model model) {
        model.addAttribute("yingbin", yingbin);
        return "/yingbin/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"yingbin:add", "yingbin:edit"})
    @ResponseBody
    public ResultVo save(@Validated YingbinValid valid, Yingbin yingbin) {
        // 复制保留无需修改的数据
        if (yingbin.getId() != null) {
            Yingbin beYingbin = yingbinService.getById(yingbin.getId());
            EntityBeanUtil.copyProperties(beYingbin, yingbin);
        }

        // 保存数据
        yingbinService.save(yingbin);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("yingbin:detail")
    public String toDetail(@PathVariable("id") Yingbin yingbin, Model model) {
        model.addAttribute("yingbin",yingbin);
        return "/yingbin/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("yingbin:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (yingbinService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}