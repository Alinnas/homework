package com.linln.admin.yingbin.service.impl;

import com.linln.admin.yingbin.domain.Yingbin;
import com.linln.admin.yingbin.repository.YingbinRepository;
import com.linln.admin.yingbin.service.YingbinService;
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
public class YingbinServiceImpl implements YingbinService {

    @Autowired
    private YingbinRepository yingbinRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Yingbin getById(Long id) {
        return yingbinRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Yingbin> getPageList(Example<Yingbin> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return yingbinRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param yingbin 实体对象
     */
    @Override
    public Yingbin save(Yingbin yingbin) {
        return yingbinRepository.save(yingbin);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return yingbinRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}