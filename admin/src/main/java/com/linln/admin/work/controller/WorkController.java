package com.linln.admin.work.controller;

import com.linln.admin.work.domain.Work;
import com.linln.admin.work.service.WorkService;
import com.linln.admin.work.validator.WorkValid;
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
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private WorkService workService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("work:index")
    public String index(Model model, Work work) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", match -> match.contains())
                .withMatcher("name", match -> match.contains());

        // 获取数据列表
        Example<Work> example = Example.of(work, matcher);
        Page<Work> list = workService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/work/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("work:add")
    public String toAdd() {
        return "/work/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("work:edit")
    public String toEdit(@PathVariable("id") Work work, Model model) {
        model.addAttribute("work", work);
        return "/work/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"work:add", "work:edit"})
    @ResponseBody
    public ResultVo save(@Validated WorkValid valid, Work work) {
        // 复制保留无需修改的数据
        if (work.getId() != null) {
            Work beWork = workService.getById(work.getId());
            EntityBeanUtil.copyProperties(beWork, work);
        }

        // 保存数据
        workService.save(work);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("work:detail")
    public String toDetail(@PathVariable("id") Work work, Model model) {
        model.addAttribute("work",work);
        return "/work/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("work:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (workService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}