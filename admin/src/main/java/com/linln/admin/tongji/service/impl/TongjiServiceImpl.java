package com.linln.admin.tongji.service.impl;

import com.linln.admin.tongji.domain.Tongji;
import com.linln.admin.tongji.repository.TongjiRepository;
import com.linln.admin.tongji.service.TongjiService;
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
public class TongjiServiceImpl implements TongjiService {

    @Autowired
    private TongjiRepository tongjiRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Tongji getById(Long id) {
        return tongjiRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Tongji> getPageList(Example<Tongji> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return tongjiRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param tongji 实体对象
     */
    @Override
    public Tongji save(Tongji tongji) {
        return tongjiRepository.save(tongji);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return tongjiRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}