package com.linln.admin.emp1.service.impl;

import com.linln.admin.emp1.domain.Emp1;
import com.linln.admin.emp1.repository.Emp1Repository;
import com.linln.admin.emp1.service.Emp1Service;
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
public class Emp1ServiceImpl implements Emp1Service {

    @Autowired
    private Emp1Repository emp1Repository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Emp1 getById(Long id) {
        return emp1Repository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Emp1> getPageList(Example<Emp1> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return emp1Repository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param emp1 实体对象
     */
    @Override
    public Emp1 save(Emp1 emp1) {
        return emp1Repository.save(emp1);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return emp1Repository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}