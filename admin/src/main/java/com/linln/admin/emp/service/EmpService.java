package com.linln.admin.emp.service;

import com.linln.admin.emp.domain.Emp;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
public interface EmpService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Emp> getPageList(Example<Emp> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Emp getById(Long id);

    /**
     * 保存数据
     * @param emp 实体对象
     */
    Emp save(Emp emp);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}