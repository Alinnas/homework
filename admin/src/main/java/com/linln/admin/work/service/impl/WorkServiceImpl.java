package com.linln.admin.work.service.impl;

import com.linln.admin.work.domain.Work;
import com.linln.admin.work.repository.WorkRepository;
import com.linln.admin.work.service.WorkService;
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
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Work getById(Long id) {
        return workRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Work> getPageList(Example<Work> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return workRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param work 实体对象
     */
    @Override
    public Work save(Work work) {
        return workRepository.save(work);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return workRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}