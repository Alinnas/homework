package com.linln.admin.setting.service.impl;

import com.linln.admin.setting.domain.Setting;
import com.linln.admin.setting.repository.SettingRepository;
import com.linln.admin.setting.service.SettingService;
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
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Setting getById(Long id) {
        return settingRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Setting> getPageList(Example<Setting> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return settingRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param setting 实体对象
     */
    @Override
    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return settingRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}