package com.linln.admin.yingbin.service;

import com.linln.admin.yingbin.domain.Yingbin;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/20
 */
public interface YingbinService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Yingbin> getPageList(Example<Yingbin> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Yingbin getById(Long id);

    /**
     * 保存数据
     * @param yingbin 实体对象
     */
    Yingbin save(Yingbin yingbin);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}