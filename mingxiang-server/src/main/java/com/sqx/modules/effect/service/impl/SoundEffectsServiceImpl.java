package com.sqx.modules.effect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.effect.dao.SoundAndUserDao;
import com.sqx.modules.effect.dao.SoundEffectsDao;
import com.sqx.modules.effect.entity.SoundAndUser;
import com.sqx.modules.effect.entity.SoundEffects;
import com.sqx.modules.effect.service.SoundEffectsService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.utils.NWayMixedAudioInputStream;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 * @description 针对表【sound_effects(音效表)】的数据库操作Service实现
 * @createDate 2024-06-19 14:05:42
 */
@Service
public class SoundEffectsServiceImpl extends ServiceImpl<SoundEffectsDao, SoundEffects>
        implements SoundEffectsService {

    @Autowired
    private SoundEffectsDao soundEffectsDao;

    @Autowired
    private SoundAndUserDao soundAndUserDao;

    @Autowired
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(SoundEffectsServiceImpl.class);


    @Override
    public Result getSoundEffectsList(Integer pageNum, Integer pageSize, String effectsName) {
        PageHelper.startPage(pageNum, pageSize);
        List<SoundEffects> list = soundEffectsDao.selectList(new QueryWrapper<SoundEffects>().like(!ObjectUtils.isEmpty(effectsName), "effects_name", "%" + effectsName + "%")
                .eq("delete_flag", 0)
                .orderByDesc("create_time"));
        return Result.success().put("data", new PageInfo<>(list));
    }

    @Override
    public Result selectSoundEffectsList(Integer pageNum, Integer pageSize, Long userId) {
        UserEntity currentUser = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(currentUser)) {
            return Result.error("当前用户未登录,请先登录");
        }
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<SoundEffects> list = soundEffectsDao.getSoundEffectsByUserChoose(currentUser.getUserId());
            return Result.success().put("data", new PageInfo<>(list));
        } else {
            List<SoundEffects> list = soundEffectsDao.getSoundEffectsByUserChoose(currentUser.getUserId());
            return Result.success().put("data", list);
        }
    }

    @Override
    public Result saveInfo(SoundEffects soundEffects) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            int count = soundEffectsDao.selectCount(new QueryWrapper<SoundEffects>().eq("effects_name", soundEffects.getEffectsName())
                    .eq("delete_flag", 0));
            if (count > 0) {
                return Result.error("该音效已存在");
            }
            soundEffects.setCreateBy(principal.getUserId());
            soundEffects.setCreateTime(new Date());
            int result = soundEffectsDao.insert(soundEffects);
            if (result <= 0) {
                return Result.error("添加失败");
            }
            return Result.success("添加成功");
        }
        return Result.error("用户不存在");
    }

    @Override
    public Result edit(SoundEffects soundEffects) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            SoundEffects soundEffect = soundEffectsDao.selectById(soundEffects.getId());
            if (ObjectUtils.isEmpty(soundEffect)) {
                return Result.error("该记录不存在");
            }
            int count = soundEffectsDao.selectCount(new QueryWrapper<SoundEffects>().eq("effects_name", soundEffects.getEffectsName())
                    .eq("delete_flag", 0)
                    .ne("id", soundEffects.getId()));
            if (count > 0) {
                return Result.error("该音效已存在");
            }
            soundEffects.setUpdateBy(principal.getUserId());
            soundEffects.setUpdateTime(new Date());
            int result = soundEffectsDao.updateById(soundEffects);
            if (result <= 0) {
                return Result.error("修改失败");
            }
            return Result.success("修改成功");
        }
        return Result.error("用户不存在");
    }

    @Override
    public Result delete(Long effectsId) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("用户不存在");
        }
        SoundEffects soundEffects = soundEffectsDao.selectById(effectsId);
        if (ObjectUtils.isEmpty(soundEffects)) {
            return Result.error("混音场景不存在");
        }
        soundEffects.setDeleteFlag(1);
        int result = soundEffectsDao.updateById(soundEffects);
        if (result <= 0) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @Override
    public Result addSoundEffectsToUser(SoundEffects soundEffects, Long userId) {
        UserEntity currentUser = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(currentUser)) {
            return Result.error("当前用户未登录,请先登录");
        }
        List<SoundAndUser> toInsert = new ArrayList<>();
        if (soundEffects.getEffectsIds() != null) {
            List<Long> effectsIds = soundEffects.getEffectsIds();
            // 批量查询音效是否存在且未被删除
            List<SoundEffects> existingSounds = soundEffectsDao.selectList(new QueryWrapper<SoundEffects>()
                    .in("id", effectsIds)
                    .eq("delete_flag", 0));

            if (existingSounds.isEmpty()) {
                return Result.error("指定的音效不存在或已被删除");
            }
            Set<Long> validEffectsIds = existingSounds.stream().map(SoundEffects::getId).collect(Collectors.toSet());
            List<Long> effectsToAdd = effectsIds.stream().filter(validEffectsIds::contains).collect(Collectors.toList());
            if (effectsToAdd.isEmpty()) {
                return Result.success("所有指定的音效均不存在或已被删除，无法关联");
            }
            List<SoundAndUser> soundAndUsers = soundAndUserDao.selectList(new QueryWrapper<SoundAndUser>()
                    .eq("user_id", userId));
            if (soundAndUsers != null && !soundAndUsers.isEmpty()) {
                for (SoundAndUser soundAndUser : soundAndUsers) {
                    if (!effectsIds.contains(soundAndUser.getEffectsId())) {
                        soundAndUser.setEffectsId(effectsIds.get(0));
                        soundAndUserDao.updateById(soundAndUser);
                    }
                }
                return Result.success("关联成功");
            } else {
                for (Long id : effectsToAdd) {
                    //这里检查是否关联到当前用户
                    int count = soundAndUserDao.selectCount(new QueryWrapper<SoundAndUser>()
                            .eq("effects_id", id)
                            .eq("user_id", userId));
                    if (count == 0) {
                        // 准备数据以便批量插入或关联操作
                        SoundAndUser association = new SoundAndUser();
                        association.setEffectsId(id);
                        association.setUserId(currentUser.getUserId());
                        // 可能还需要设置其他字段，如关联创建时间等
                        toInsert.add(association);
                    }
                }
                if (!toInsert.isEmpty()) {
                    int result = soundAndUserDao.insertBatch(toInsert);
                    if (result <= 0) {
                        return Result.error("关联失败");
                    }
                    return Result.success("关联成功");
                } else {
                    // 如果没有新的音效需要关联
                    return Result.success("该混音场景已关联，无需重复操作");
                }
            }
        } else {
            return Result.error("请选择音效");
        }
    }

    @Override
    public Result getSoundEffectsByUserId(Long userId) {
        UserEntity currentUser = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(currentUser)) {
            return Result.error("当前用户未登录,请先登录");
        }
        List<SoundEffects> list = soundEffectsDao.getSoundEffectsByUserId(currentUser.getUserId());
        if (ObjectUtils.isEmpty(list)) {
            return Result.success().put("data", Collections.EMPTY_LIST);
        }
        return Result.success().put("data", list);
    }

    @Override
    public void receiveMp3(List<String> audioUrls, HttpServletResponse response) throws IOException {
        if (audioUrls == null || audioUrls.isEmpty()) {
            response.sendError(500, "至少需要一个音频URL");
            return;
        }
        if (audioUrls.size() > 10) {
            response.sendError(500, "最多支持10个音频");
            return;
        }
        // 1. 设置响应头
        response.setContentType("audio/mpeg");
        response.setHeader("Content-Disposition", "inline; filename=mixed.mp3");
        // 统一管理所有流
        List<AudioInputStream> allStreams = new ArrayList<>();
        try {
            // 2. 存储所有解码后的 PCM 流
            List<AudioInputStream> pcmStreams = new ArrayList<>();
            // 3. 遍历所有 URL，下载并解码为 PCM
            for (String urlString : audioUrls) {
                URL url = new URL(urlString);
                // 获取音频输入流 需要 mp3spi 支持
                AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(url);
                // 转换为 PCM 格式 统一格式
//                AudioInputStream pcmStream = convertToPcmFormat(mp3Stream);
                pcmStreams.add(mp3Stream);
                allStreams.add(mp3Stream);
            }
            // 4. 创建目标格式(统一格式（可选：以第一个为准，其他转成一样）)
            AudioFormat targetFormat = pcmStreams.get(0).getFormat();
            List<AudioInputStream> formatStreams = new ArrayList<>();
            for (AudioInputStream stream : pcmStreams) {
                AudioInputStream converted = AudioSystem.getAudioInputStream(targetFormat, stream);
                formatStreams.add(converted);
                allStreams.add(converted);
            }
            // 5.创建混合流(关键！支持N个输入)
            AudioInputStream mixedStream = new NWayMixedAudioInputStream(formatStreams, targetFormat);
            allStreams.add(mixedStream);
            // 6.使用JAVE 编码混合流为 MP3 并直接输出到响应
            encodeToMp3Stream(mixedStream, response.getOutputStream(), targetFormat);
        } catch (Exception e) {
            // 7.记录错误（生产环境建议使用日志）
            logger.error("音频混合过程中发生异常", e);
            // 发送错误（仅当响应尚未提交）
            if (!response.isCommitted()) {
                sendError(response, "音频混合失败: " + e.getMessage());
            }
        } finally {
            // 8.安全关闭所有输入流
            closeStreams(allStreams);
        }
    }

    private void encodeToMp3Stream(AudioInputStream pcStream, OutputStream out, AudioFormat sourceFormat) throws IOException {
        // 创建临时 WAV 文件
        File tempWavFile = File.createTempFile("mixed_audio", ".wav");
        tempWavFile.deleteOnExit();
        // 将混合后的 PCM 流写入 WAV 文件
        AudioSystem.write(pcStream, AudioFileFormat.Type.WAVE, tempWavFile);
        // 创建输出 MP3 文件
        File tempMp3File = File.createTempFile("encoded_audio", ".mp3");
        tempMp3File.deleteOnExit();
        // 1. 设置音频编码参数
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(sourceFormat.getChannels());
        audio.setSamplingRate((int) sourceFormat.getSampleRate());

        // 2.设置编码参数
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);

        // 3. 禁用视频编码
        VideoAttributes video = new VideoAttributes();
        video.setCodec(null);
        attrs.setVideoAttributes(video);

//        // 4. 创建输入源（支持 AudioInputStream）
//        MultimediaObject multimediaObject = new MultimediaObject(pcStream, sourceFormat);

        // 5. 执行编码并输出到响应流
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(tempWavFile), tempMp3File, attrs);
            // 将生成的 MP3 文件内容写入响应输出流
            byte[] mp3Data = java.nio.file.Files.readAllBytes(tempMp3File.toPath());
            out.write(mp3Data);
            out.flush();
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        } finally {
            // 删除临时文件
            if (!tempWavFile.delete()) {
                logger.warn("临时WAV文件删除失败: {}", tempWavFile.getAbsolutePath());
            }
            if (!tempMp3File.delete()) {
                logger.warn("临时MP3文件删除失败: {}", tempMp3File.getAbsolutePath());
            }
        }
    }

    // 发送错误响应
    private void sendError(HttpServletResponse response, String message) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(500);
            response.getWriter().write(String.format("{\"code\":%d,\"msg\":\"%s\"}", 500, message));
        } catch (IOException e) {
            logger.error("发送错误响应异常", e);
        }
    }

    // 安全关闭所有流
    private void closeStreams(List<AudioInputStream> allStreams) {
        for (AudioInputStream allStream : allStreams) {
            try {
                if (allStream != null) {
                    allStream.close();
                }
            } catch (IOException e) {
                logger.error("关闭流异常", e);
            }
        }
    }
}




