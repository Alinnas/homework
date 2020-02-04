package com.linln.admin.lishi.service;

import com.linln.admin.lishi.domain.Ilshi;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/20
 */
public interface IlshiService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Ilshi> getPageList(Example<Ilshi> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Ilshi getById(Long id);

    /**
     * 保存数据
     * @param ilshi 实体对象
     */
    Ilshi save(Ilshi ilshi);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}