package com.sqx.modules.effect.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.effect.entity.SoundEffects;
import com.sqx.modules.effect.service.SoundEffectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/19 17:15
 */
@RestController
@RequestMapping("/soundEffects")
@Api(value = "后台混音场景管理相关", tags = {"后台混音场景管理相关"})
public class SoundEffectsController {

    @Autowired
    private SoundEffectsService soundEffectsService;

    @GetMapping("/getSoundEffectsList")
    @ApiOperation("管理后台获取混音场景列表")
    public Result getSoundEffectsList(@RequestParam(value = "pageNum") Integer pageNum,
                                      @RequestParam(value = "pageSize") Integer pageSize,
                                      @RequestParam(value = "effectsName",required = false) String effectsName) {
        return soundEffectsService.getSoundEffectsList(pageNum, pageSize, effectsName);
    }

    @PostMapping("/saveInfo")
    @ApiOperation("管理后台添加混音场景")
    public Result saveInfo(@RequestBody SoundEffects soundEffects) {
        return soundEffectsService.saveInfo(soundEffects);
    }

    @PostMapping("/edit")
    @ApiOperation("管理后台编辑混音场景")
    public Result edit(@RequestBody SoundEffects soundEffects) {
        return soundEffectsService.edit(soundEffects);
    }

    @PostMapping("/delete")
    @ApiOperation("管理后台删除混音场景")
    public Result delete(@RequestBody SoundEffects soundEffects) {
        return soundEffectsService.delete(soundEffects.getId());
    }

}
