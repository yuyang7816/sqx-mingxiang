package com.sqx.modules.utils.translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    // 这里使用静态，让 service 属于类
    private static CommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(CommonInfoService commonInfoService) {
        TransApi.commonInfoService = commonInfoService;
    }


    /**
     * 翻译
     * @param query 单次请求长度控制在 6000 bytes以内（汉字约为输入参数 2000 个）
     * @param from
     * @param to
     * @return
     * @throws UnsupportedEncodingException
     */
    public static List<TransResult> getTransResult(String query, String from, String to) throws UnsupportedEncodingException {

        Map<String, String> params = buildParams(query, from, to);
        String responseText = HttpGet.get(TRANS_API_HOST, params);
        JSONObject jsonObject = JSONObject.parseObject(responseText);
        String transResult = jsonObject.getString("trans_result");
        return JSON.parseArray(transResult, TransResult.class);


    }

    private static Map<String, String> buildParams(String query, String from, String to) throws UnsupportedEncodingException {
        String appid = commonInfoService.findOne(422).getValue();     // 您的应用ID
        String securityKey = commonInfoService.findOne(423).getValue();
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
