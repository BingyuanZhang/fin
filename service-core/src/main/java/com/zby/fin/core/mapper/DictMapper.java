package com.zby.fin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zby.fin.core.pojo.dto.ExcelDictDTO;
import com.zby.fin.core.pojo.entity.Dict;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
public interface DictMapper extends BaseMapper<Dict> {
    void insertBatch(List<ExcelDictDTO> list);
}
