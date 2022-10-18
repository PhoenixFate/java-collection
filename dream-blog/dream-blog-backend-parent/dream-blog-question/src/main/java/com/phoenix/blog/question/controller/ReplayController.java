package com.phoenix.blog.question.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Replay;
import com.phoenix.blog.question.service.IReplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 回答信息表 前端控制器
 * </p>
 *
 * @author phoenix
 */
@Api(tags = "回答管理接口")
@RestController
@RequestMapping("/replay")
@AllArgsConstructor
public class ReplayController {

    private final IReplayService replayService;

    @ApiOperation("删除回答评论接口")
    @ApiImplicitParam(name = "id", value = "回答评论id", required = true)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return replayService.deleteById(id);
    }

    @ApiOperation("新增回答信息接口")
    @PostMapping
    public Result add(@RequestBody Replay replay) {
        return replayService.add(replay);
    }
}

