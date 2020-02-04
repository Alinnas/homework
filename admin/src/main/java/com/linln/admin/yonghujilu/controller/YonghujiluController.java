package com.linln.admin.yonghujilu.controller;

import com.linln.admin.yonghujilu.domain.Yonghujilu;
import com.linln.admin.yonghujilu.service.YonghujiluService;
import com.linln.admin.yonghujilu.validator.YonghujiluValid;
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
@RequestMapping("/yonghujilu/yonghujilu")
public class YonghujiluController {

    @Autowired
    private YonghujiluService yonghujiluService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("yonghujilu:yonghujilu:index")
    public String index(Model model, Yonghujilu yonghujilu) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Yonghujilu> example = Example.of(yonghujilu, matcher);
        Page<Yonghujilu> list = yonghujiluService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/yonghujilu/yonghujilu/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("yonghujilu:yonghujilu:add")
    public String toAdd() {
        return "/yonghujilu/yonghujilu/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("yonghujilu:yonghujilu:edit")
    public String toEdit(@PathVariable("id") Yonghujilu yonghujilu, Model model) {
        model.addAttribute("yonghujilu", yonghujilu);
        return "/yonghujilu/yonghujilu/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"yonghujilu:yonghujilu:add", "yonghujilu:yonghujilu:edit"})
    @ResponseBody
    public ResultVo save(@Validated YonghujiluValid valid, Yonghujilu yonghujilu) {
        // 复制保留无需修改的数据
        if (yonghujilu.getId() != null) {
            Yonghujilu beYonghujilu = yonghujiluService.getById(yonghujilu.getId());
            EntityBeanUtil.copyProperties(beYonghujilu, yonghujilu);
        }

        // 保存数据
        yonghujiluService.save(yonghujilu);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("yonghujilu:yonghujilu:detail")
    public String toDetail(@PathVariable("id") Yonghujilu yonghujilu, Model model) {
        model.addAttribute("yonghujilu",yonghujilu);
        return "/yonghujilu/yonghujilu/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("yonghujilu:yonghujilu:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (yonghujiluService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}