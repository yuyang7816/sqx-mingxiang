package com.sqx.modules.effect.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.effect.entity.SoundEffects;
import com.sqx.modules.effect.service.SoundEffectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/6/19 16:21
 */
@RestController
@RequestMapping("/app/soundEffects")
@Api(value = "app音效相关", tags = {"app音效相关"})
public class AppSoundEffectsController {

    @Autowired
    private SoundEffectsService soundEffectsService;

    @Login
    @GetMapping("/selectSoundEffectsList")
    @ApiOperation("获取app用户是否选中混音场景的列表")
    public Result selectSoundEffectsList(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                         @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                         @RequestAttribute("userId") Long userId) {
        return soundEffectsService.selectSoundEffectsList(pageNum, pageSize, userId);
    }

    @Login
    @PostMapping("/addSoundEffectsToUser")
    @ApiOperation("添加混音场景到app用户")
    public Result addSoundEffectsToUser(@RequestBody SoundEffects soundEffects,
                                        @RequestAttribute("userId") Long userId) {
        return soundEffectsService.addSoundEffectsToUser(soundEffects, userId);
    }

    @Login
    @GetMapping("/getSoundEffectsByUserId")
    @ApiOperation("获取app用户已添加的混音场景列表")
    public Result getSoundEffectsByUserId(@RequestAttribute("userId") Long userId) {
        return soundEffectsService.getSoundEffectsByUserId(userId);
    }

    @GetMapping("/receiveMp3")
    @ApiOperation("接收前端传过来的mp3路径混合成新的音效")
    public void receiveMp3(@RequestParam("audioUrls")List<String> audioUrls, HttpServletResponse response) throws IOException {
         soundEffectsService.receiveMp3(audioUrls,response);
    }




}
