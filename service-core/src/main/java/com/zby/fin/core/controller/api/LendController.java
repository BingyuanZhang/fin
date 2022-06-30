package com.zby.fin.core.controller.api;


import com.zby.common.result.R;
import com.zby.fin.core.pojo.entity.Lend;
import com.zby.fin.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
@Api(tags = "标的")
@RestController
@RequestMapping("/api/core/lend")
@Slf4j
public class LendController {

    @Resource
    private LendService lendService;

    @ApiOperation("获取标的列表")
    @GetMapping("/list")
    public R  list() {
        List<Lend> lendList = lendService.selectList();
        return R.ok().data("lendList", lendList);
    }
}