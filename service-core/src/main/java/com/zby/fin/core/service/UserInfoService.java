package com.zby.fin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zby.fin.core.pojo.entity.UserInfo;
import com.zby.fin.core.pojo.query.UserInfoQuery;
import com.zby.fin.core.pojo.vo.LoginVO;
import com.zby.fin.core.pojo.vo.RegisterVO;
import com.zby.fin.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface UserInfoService extends IService<UserInfo> {
    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO, String ip);

    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);
}
