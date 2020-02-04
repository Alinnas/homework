package com.linln.admin.yellowlist.service.impl;

import com.linln.admin.yellowlist.domain.Yellowlist;
import com.linln.admin.yellowlist.repository.YellowlistRepository;
import com.linln.admin.yellowlist.service.YellowlistService;
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
public class YellowlistServiceImpl implements YellowlistService {

    @Autowired
    private YellowlistRepository yellowlistRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Yellowlist getById(Long id) {
        return yellowlistRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Yellowlist> getPageList(Example<Yellowlist> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return yellowlistRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param yellowlist 实体对象
     */
    @Override
    public Yellowlist save(Yellowlist yellowlist) {
        return yellowlistRepository.save(yellowlist);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return yellowlistRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}