package com.zby.fin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zby.fin.core.pojo.entity.BorrowInfo;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {
    List<BorrowInfo> selectBorrowInfoList();
}
