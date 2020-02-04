package com.linln.admin.yonghujilu.service;

import com.linln.admin.yonghujilu.domain.Yonghujilu;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/20
 */
public interface YonghujiluService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Yonghujilu> getPageList(Example<Yonghujilu> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Yonghujilu getById(Long id);

    /**
     * 保存数据
     * @param yonghujilu 实体对象
     */
    Yonghujilu save(Yonghujilu yonghujilu);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}