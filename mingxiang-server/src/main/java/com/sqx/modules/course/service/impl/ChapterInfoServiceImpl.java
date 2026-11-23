package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.course.entity.ChapterInfo;
import com.sqx.modules.course.dao.ChapterInfoDao;
import com.sqx.modules.course.entity.CourseInfo;
import com.sqx.modules.course.service.ChapterInfoService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 章节信息(ChapterInfo)表服务实现类
 *
 * @author makejava
 * @since 2024-05-16 15:20:27
 */
@Service("chapterInfoService")
public class ChapterInfoServiceImpl extends ServiceImpl<ChapterInfoDao, ChapterInfo> implements ChapterInfoService {
    @Autowired
    private ChapterInfoDao chapterInfoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserVipDao userVipDao;


    @Override
    public Result selectChapterInfoList(Integer pageNum, Integer pageSize, String chapterName,Long courseId) {
        PageHelper.startPage(pageNum, pageSize);
        List<ChapterInfo> chapterInfoList = chapterInfoDao.selectList(new QueryWrapper<ChapterInfo>()
                .like(!ObjectUtils.isEmpty(chapterName),"chapter_name", "%" + chapterName + "%")
                .eq(!ObjectUtils.isEmpty(courseId),"course_id", courseId)
                .eq("delete_flag", 0));
        return Result.success().put("data", new PageInfo<>(chapterInfoList));
    }

    @Override
    public Result saveInfo(ChapterInfo chapterInfo) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        // 检查是否有重名的
        int count = chapterInfoDao.selectCount(new QueryWrapper<ChapterInfo>().eq("chapter_name", chapterInfo.getChapterName())
                .eq("delete_flag", 0));
        if (count > 0) {
            return Result.error("该章节名称已存在");
        }
        chapterInfo.setCreateBy(principal.getUserId());
        chapterInfo.setCreateTime(new Date());
        int result = chapterInfoDao.insert(chapterInfo);
        if (result <= 0) {
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result edit(ChapterInfo chapterInfo) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        // 检查名称是否重复
        int count = chapterInfoDao.selectCount(new QueryWrapper<ChapterInfo>().eq("chapter_name", chapterInfo.getChapterName())
                .eq("delete_flag", 0)
                .ne("id", chapterInfo.getId()));
        if (count > 0) {
            return Result.error("该课程名称已存在");
        }
        ChapterInfo chapterInfo1 = chapterInfoDao.selectById(chapterInfo.getId());
        if (ObjectUtils.isEmpty(chapterInfo1)) {
            return Result.error("该章节不存在");
        }
        chapterInfo.setUpdateBy(principal.getUserId());
        chapterInfo.setUpdateTime(new Date());
        int result = chapterInfoDao.updateById(chapterInfo);
        if (result <= 0) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result delete(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在");
        }
        ChapterInfo chapterInfo = chapterInfoDao.selectById(id);
        if (ObjectUtils.isEmpty(chapterInfo)) {
            return Result.error("该章节不存在");
        }
        chapterInfo.setDeleteFlag(1);
        if (chapterInfoDao.updateById(chapterInfo) <= 0) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @Override
    public Result selectDetail(Long id) {
        ChapterInfo chapterInfo = chapterInfoDao.selectById(id);
        if (ObjectUtils.isEmpty(chapterInfo)) {
            return Result.error("该章节不存在");
        }
        return Result.success().put("data", chapterInfo);
    }

    @Override
    public Result selectDetailByApp(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.error("当前用户未登录,请先登录");
        }
        ChapterInfo chapterInfo = chapterInfoDao.selectById(id);
        if (ObjectUtils.isEmpty(chapterInfo)) {
            return Result.error("该章节不存在");
        }
        // 判断该章节是不是收费
        // 收费就判断是不是会员，是会员允许访问，不是会员不允许访问
            if (chapterInfo.getChapterCharge() == 1){
                UserVip userVip = userVipDao.selectByUserId(userId);
                if (ObjectUtils.isEmpty(userVip)){
                    return Result.error("请先购买会员");
                }
            }
        chapterInfo.setChapterSeveral("第"+chapterInfo.getChapterNum()+"章");
        return Result.success().put("data", chapterInfo);
    }


}
