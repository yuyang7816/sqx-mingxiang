package com.sqx.modules.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.configI18n.entity.ConfigI18n;
import com.sqx.modules.configI18n.service.ConfigI18nService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
@Component
public class DataSourceMessageSource extends AbstractMessageSource implements ResourceLoaderAware {

    ResourceLoader resourceLoader;

    // 这个是用来缓存数据库中获取到的配置的 数据库配置更改的时候可以调用reload方法重新加载
    private static final HashMap<String, Map<String, String>> LOCAL_CACHE = new HashMap<>();
    //缓存总数
    private int i18nCount = 0;

    @Resource
    private ConfigI18nService configI18nService;
    @Resource
    private CommonInfoService commonInfoService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 初始化
     * Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行，init（）方法之前执行。
     */
    @PostConstruct
    public void init() {
        this.reload();
    }

    /**
     * 重新将数据库中的国际化配置加载
     */
    public void reload() {
        LOCAL_CACHE.clear();
        LOCAL_CACHE.putAll(loadAllMessageResourcesFromDB());
    }

    /**
     * 从数据库中获取所有国际化配置 这边可以根据自己数据库表结构进行相应的业务实现
     * 对应的语言能够取出来对应的值就行了 无需一定要按照这个方法来
     */
    public Map<String, Map<String, String>> loadAllMessageResourcesFromDB() {
        log.info("正在缓存语言数据...");
        int oldCount = i18nCount;
        long start = System.currentTimeMillis();
        // 获取数据库配置
        List<ConfigI18n> list = configI18nService.list(new QueryWrapper<ConfigI18n>().eq("show_type", 1));
        i18nCount = list.size();
        if (!list.isEmpty()) {
            //获取总共有多少个语种
            List<HashMap<String, String>> languageList = configI18nService.getLanguageList(1);
            for (HashMap<String, String> map : languageList) {
                String language = map.get("language");
                HashMap<String, String> hashMap = new HashMap<>(list.size());
                for (ConfigI18n item : list) {
                    if (language.equals(item.getLanguage())) {
                        hashMap.put(item.getPrimaryKey(), item.getLanguageValue());
                    }
                }
                LOCAL_CACHE.put(language, hashMap);
            }
        }
        long end = System.currentTimeMillis();
        log.info("缓存完成,缓存共" + list.size() + "个,新增" + (i18nCount - oldCount) + "个,总耗时" + ((end - start) / 1000) + "秒");
        return new HashMap<>();
    }

    /**
     * 从缓存中取出国际化配置对应的数据 或者从父级获取
     *
     * @param code
     * @param str
     * @param locale 可以为null, 表示从当前HttpServletRequest中获取语言
     * @return
     */
    public String getSourceFromCache(String code, String[] str, Locale locale, String... zh) {
        String defaultLanguage = commonInfoService.findOne(871).getValue();
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String language = locale == null ? request.getLocale().getLanguage() : locale.getLanguage();
        String originalStr;
        // 获取缓存中对应语言的所有数据项
        Map<String, String> props = LOCAL_CACHE.get(language);
        if (zh != null && zh.length > 0) {
            props = LOCAL_CACHE.get(defaultLanguage);
        } else {
            if (null == props || !props.containsKey(code)) {
                //如果不存在则默认返回中文
                props = LOCAL_CACHE.get(defaultLanguage);
            }
        }

        originalStr = props.get(code);
        //如果还不存在就原样返回
        if (StringUtils.isBlank(originalStr)) {
            originalStr = code;
        }
        if (str != null && str.length > 0) {
            StringBuffer sb = new StringBuffer();
            String regex = "\\$\\{(.+?)\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(originalStr);
            int i = 0;
            while (m.find()) {
                m.appendReplacement(sb, str[i]);
                i++;
            }
            m.appendTail(sb);
            originalStr = sb.toString();
        }

        return originalStr;
    }

    // 下面三个重写的方法是比较重要的
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getSourceFromCache(code, null, locale);
        return new MessageFormat(msg, locale);
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return getSourceFromCache(code, null, locale);
    }

}