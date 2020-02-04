package com.linln.admin.yellowlist.service;

import com.linln.admin.yellowlist.domain.Yellowlist;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
public interface YellowlistService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Yellowlist> getPageList(Example<Yellowlist> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Yellowlist getById(Long id);

    /**
     * 保存数据
     * @param yellowlist 实体对象
     */
    Yellowlist save(Yellowlist yellowlist);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}