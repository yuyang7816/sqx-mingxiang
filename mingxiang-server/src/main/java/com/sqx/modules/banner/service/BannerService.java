package com.sqx.modules.banner.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.banner.entity.Banner;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> selectBannerList(Integer classify, Integer type);

    List<Banner> selectBannerLists(Integer classify);

    int saveBody(String image, String url, Integer sort);

    Banner selectBannerById(Long id);

    Result updateBannerStateById(Long id);

    int updateBannerById(Banner banner);

    int insertBanner(Banner banner);

    PageUtils selectPage(Integer page, Integer limit, Integer classify, Integer state, Integer type);

}
