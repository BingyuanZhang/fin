package com.zby.fin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zby.fin.core.pojo.entity.UserBind;
import com.zby.fin.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface UserBindService extends IService<UserBind> {
    /**
     * 账户绑定提交到托管平台的数据
     */
    String commitBindUser(UserBindVO userBindVO, Long userId);

    void notify(Map<String, Object> paramMap);
}
