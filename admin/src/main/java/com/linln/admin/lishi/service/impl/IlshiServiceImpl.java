package com.linln.admin.lishi.service.impl;

import com.linln.admin.lishi.domain.Ilshi;
import com.linln.admin.lishi.repository.IlshiRepository;
import com.linln.admin.lishi.service.IlshiService;
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
 * @date 2020/01/20
 */
@Service
public class IlshiServiceImpl implements IlshiService {

    @Autowired
    private IlshiRepository ilshiRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Ilshi getById(Long id) {
        return ilshiRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Ilshi> getPageList(Example<Ilshi> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return ilshiRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param ilshi 实体对象
     */
    @Override
    public Ilshi save(Ilshi ilshi) {
        return ilshiRepository.save(ilshi);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return ilshiRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}