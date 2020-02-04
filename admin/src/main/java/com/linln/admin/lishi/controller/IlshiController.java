package com.linln.admin.lishi.controller;

import com.linln.admin.lishi.domain.Ilshi;
import com.linln.admin.lishi.service.IlshiService;
import com.linln.admin.lishi.validator.IlshiValid;
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
@RequestMapping("/lishi")
public class IlshiController {

    @Autowired
    private IlshiService ilshiService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("lishi:index")
    public String index(Model model, Ilshi ilshi) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("title", match -> match.contains());

        // 获取数据列表
        Example<Ilshi> example = Example.of(ilshi, matcher);
        Page<Ilshi> list = ilshiService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/lishi/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("lishi:add")
    public String toAdd() {
        return "/lishi/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("lishi:edit")
    public String toEdit(@PathVariable("id") Ilshi ilshi, Model model) {
        model.addAttribute("ilshi", ilshi);
        return "/lishi/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"lishi:add", "lishi:edit"})
    @ResponseBody
    public ResultVo save(@Validated IlshiValid valid, Ilshi ilshi) {
        // 复制保留无需修改的数据
        if (ilshi.getId() != null) {
            Ilshi beIlshi = ilshiService.getById(ilshi.getId());
            EntityBeanUtil.copyProperties(beIlshi, ilshi);
        }

        // 保存数据
        ilshiService.save(ilshi);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("lishi:detail")
    public String toDetail(@PathVariable("id") Ilshi ilshi, Model model) {
        model.addAttribute("ilshi",ilshi);
        return "/lishi/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("lishi:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (ilshiService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}