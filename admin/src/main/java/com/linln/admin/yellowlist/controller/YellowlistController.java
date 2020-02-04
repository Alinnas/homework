package com.linln.admin.yellowlist.controller;

import com.linln.admin.yellowlist.domain.Yellowlist;
import com.linln.admin.yellowlist.service.YellowlistService;
import com.linln.admin.yellowlist.validator.YellowlistValid;
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
@RequestMapping("/yellowlist")
public class YellowlistController {

    @Autowired
    private YellowlistService yellowlistService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("yellowlist:index")
    public String index(Model model, Yellowlist yellowlist) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", match -> match.contains())
                .withMatcher("updateDate", match -> match.contains())
                .withMatcher("end_date", match -> match.contains())
                .withMatcher("name", match -> match.contains());

        // 获取数据列表
        Example<Yellowlist> example = Example.of(yellowlist, matcher);
        Page<Yellowlist> list = yellowlistService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/yellowlist/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("yellowlist:add")
    public String toAdd() {
        return "/yellowlist/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("yellowlist:edit")
    public String toEdit(@PathVariable("id") Yellowlist yellowlist, Model model) {
        model.addAttribute("yellowlist", yellowlist);
        return "/yellowlist/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"yellowlist:add", "yellowlist:edit"})
    @ResponseBody
    public ResultVo save(@Validated YellowlistValid valid, Yellowlist yellowlist) {
        // 复制保留无需修改的数据
        if (yellowlist.getId() != null) {
            Yellowlist beYellowlist = yellowlistService.getById(yellowlist.getId());
            EntityBeanUtil.copyProperties(beYellowlist, yellowlist);
        }

        // 保存数据
        yellowlistService.save(yellowlist);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("yellowlist:detail")
    public String toDetail(@PathVariable("id") Yellowlist yellowlist, Model model) {
        model.addAttribute("yellowlist",yellowlist);
        return "/yellowlist/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("yellowlist:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (yellowlistService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}