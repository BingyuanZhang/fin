package com.zby.fin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zby.fin.core.pojo.entity.UserLoginRecord;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {
    List<UserLoginRecord> listTop50(Long userId);

}
