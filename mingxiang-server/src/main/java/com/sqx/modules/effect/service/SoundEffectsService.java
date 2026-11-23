package com.sqx.modules.effect.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.effect.entity.SoundEffects;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author Admin
* @description 针对表【sound_effects(音效表)】的数据库操作Service
* @createDate 2024-06-19 14:05:42
*/
public interface SoundEffectsService extends IService<SoundEffects> {

    /**
     * 管理后台获取混音场景列表
     * @param pageNum
     * @param pageSize
     * @param effectsName
     * @return
     */
    Result getSoundEffectsList(Integer pageNum, Integer pageSize, String effectsName);

    /**
     * 获取app用户是否选中混音场景的列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    Result selectSoundEffectsList(Integer pageNum, Integer pageSize,Long userId);

    /**
     * 添加混音场景
     * @param soundEffects
     * @reutrn
     */
    Result saveInfo(SoundEffects soundEffects);

    /**
     * 编辑混音场景
     * @param soundEffects
     * @return
     */
    Result edit(SoundEffects soundEffects);

    /**
     * 删除混音场景
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 添加混音场景到app用户
     * @param soundEffects
     * @param userId
     * @return
     */
    Result addSoundEffectsToUser(SoundEffects soundEffects,Long userId);

    /**
     * 获取app用户已添加的混音场景列表
     * @param userId
     * @return
     */
    Result getSoundEffectsByUserId(Long userId);

    /**
     * 接收前端传过来的mp3路径混合成新的音效
     * @param audioUrls
     * @return
     */
    void receiveMp3(List<String> audioUrls, HttpServletResponse response) throws IOException;
}
