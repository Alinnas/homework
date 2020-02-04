package com.linln.admin.kaoqinsetting.service.impl;

import com.linln.admin.kaoqinsetting.domain.Kaoqinsetting;
import com.linln.admin.kaoqinsetting.repository.KaoqinsettingRepository;
import com.linln.admin.kaoqinsetting.service.KaoqinsettingService;
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
public class KaoqinsettingServiceImpl implements KaoqinsettingService {

    @Autowired
    private KaoqinsettingRepository kaoqinsettingRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Kaoqinsetting getById(Long id) {
        return kaoqinsettingRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Kaoqinsetting> getPageList(Example<Kaoqinsetting> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return kaoqinsettingRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param kaoqinsetting 实体对象
     */
    @Override
    public Kaoqinsetting save(Kaoqinsetting kaoqinsetting) {
        return kaoqinsettingRepository.save(kaoqinsetting);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return kaoqinsettingRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}