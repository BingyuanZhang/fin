package com.zby.fin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zby.fin.core.pojo.entity.BorrowInfo;
import com.zby.fin.core.pojo.entity.Lend;
import com.zby.fin.core.pojo.vo.BorrowInfoApprovalVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface LendService extends IService<Lend> {
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);

    List<Lend> selectList();

    Map<String, Object> getLendDetail(Long id);
}
