package com.zby.fin.core.service.impl;

import com.zby.fin.core.pojo.entity.UserAccount;
import com.zby.fin.core.mapper.UserAccountMapper;
import com.zby.fin.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
