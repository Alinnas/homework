package com.linln.admin.emp1.service;

import com.linln.admin.emp1.domain.Emp1;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
public interface Emp1Service {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Emp1> getPageList(Example<Emp1> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Emp1 getById(Long id);

    /**
     * 保存数据
     * @param emp1 实体对象
     */
    Emp1 save(Emp1 emp1);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}