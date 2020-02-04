package com.linln.admin.order.service.impl;

import com.linln.admin.order.domain.SysVisitor;
import com.linln.admin.order.repository.SysVisitorRepository;
import com.linln.admin.order.service.SysVisitorService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/17
 */
@Service
public class SysVisitorServiceImpl implements SysVisitorService {

    @Autowired
    private SysVisitorRepository sysVisitorRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public SysVisitor getById(Integer id) {
        return sysVisitorRepository.findById(Long.valueOf(id)).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<SysVisitor> getPageList(Example<SysVisitor> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return sysVisitorRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param sysVisitor 实体对象
     */
    @Override
    public SysVisitor save(SysVisitor sysVisitor) {
        return sysVisitorRepository.save(sysVisitor);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return sysVisitorRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}