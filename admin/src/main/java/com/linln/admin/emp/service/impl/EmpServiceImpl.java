package com.linln.admin.emp.service.impl;

import com.linln.admin.emp.domain.Emp;
import com.linln.admin.emp.repository.EmpRepository;
import com.linln.admin.emp.service.EmpService;
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
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpRepository empRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Emp getById(Long id) {
        return empRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Emp> getPageList(Example<Emp> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return empRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param emp 实体对象
     */
    @Override
    public Emp save(Emp emp) {
        return empRepository.save(emp);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return empRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}