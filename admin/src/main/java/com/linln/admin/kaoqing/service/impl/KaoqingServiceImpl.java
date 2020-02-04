package com.linln.admin.kaoqing.service.impl;

import com.linln.admin.kaoqing.domain.Kaoqing;
import com.linln.admin.kaoqing.repository.KaoqingRepository;
import com.linln.admin.kaoqing.service.KaoqingService;
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
public class KaoqingServiceImpl implements KaoqingService {

    @Autowired
    private KaoqingRepository kaoqingRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Kaoqing getById(Long id) {
        return kaoqingRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Kaoqing> getPageList(Example<Kaoqing> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return kaoqingRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param kaoqing 实体对象
     */
    @Override
    public Kaoqing save(Kaoqing kaoqing) {
        return kaoqingRepository.save(kaoqing);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return kaoqingRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}