package com.linln.admin.setting.service;

import com.linln.admin.setting.domain.Setting;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小王子
 * @date 2020/01/20
 */
public interface SettingService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Setting> getPageList(Example<Setting> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Setting getById(Long id);

    /**
     * 保存数据
     * @param setting 实体对象
     */
    Setting save(Setting setting);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}