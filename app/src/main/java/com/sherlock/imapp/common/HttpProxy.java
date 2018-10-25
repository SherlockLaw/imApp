package com.sherlock.imapp.common;

import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.Configure;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.HttpUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class HttpProxy {

    public static String get(String urlStr, Map<String, Object> params, Map<String, Object> headers){
        addToken(urlStr,params);
        String httpResult = HttpUtil.get(Configure.getBaseUrl()+urlStr, params, headers);
        return after(httpResult);
    }

    public static String post(String urlStr, Map<String, Object> params, Map<String, Object> headers){
        addToken(urlStr,params);
        String httpResult = HttpUtil.post(Configure.getBaseUrl()+urlStr, params, headers);
        return after(httpResult);
    }
    private static void addToken(String urlStr, Map<String, Object> params){
        if (UrlConstant.isUrlNeedToken(urlStr)) {
            params.put("token", MyApplication.getToken());
        }
    }
    //对result的统一解析
    private static String after(String httpResult){
        JSONObject job = JSONObject.parseObject(httpResult);
        int code = job.getIntValue("code");
        if (code == Result.SUCCESS_CODE) {
            return job.getString("data");
        } else {
            throw new ServiceException(job.getString("msg"));
        }
    }
    public static Bitmap getImageBitmap(String url){
        return HttpUtil.getImageBitmap(url);
    }

    public static String getParamStrByArray(List<?extends Object> list){
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o.toString()).append(',');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
