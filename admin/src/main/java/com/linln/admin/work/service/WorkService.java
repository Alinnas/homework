package com.linln.admin.work.service;

import com.linln.admin.work.domain.Work;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
public interface WorkService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Work> getPageList(Example<Work> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Work getById(Long id);

    /**
     * 保存数据
     * @param work 实体对象
     */
    Work save(Work work);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}