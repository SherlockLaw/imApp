package com.sherlock.imapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.sherlock.imapp.common.ServiceException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

/**
 * http 工具类
 * Created by Administrator on 2018/5/3 0003.
 */

public class HttpUtil {

    private static final String URL_ERROR = "url错误";

    private static final String PARAM_ERROR = "参数错误";

    private static final String COMMON_WARNING = "无法打开链接";

    private static final String METHOD_ERROR = "HTTP方法错误";

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    public static String get(String urlStr, Map<String, Object> params, Map<String, Object> headers) {
        String body = genBodyByParams(params);
        if (!StringUtil.isBlank(body)) {
            urlStr = urlStr + "?" + body;
        }
        HttpURLConnection connection = commonSet(METHOD_GET, urlStr);
        return commonGet(connection);
    }

    public static String post(String urlStr, Map<String, Object> params, Map<String, Object> headers) {
        HttpURLConnection connection = commonSet(METHOD_POST, urlStr);
        try {
            String boundary = "----"+ UUID.randomUUID().toString().replace("-","");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            connection.setRequestProperty("cache-control","no-cache");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept", "*/*");

            StringBuilder sb = new StringBuilder();
            String changeRow = "\r\n";
            if (params != null && !params.isEmpty()) {
                connection.setDoOutput(true);
                FileNameMap fileNameMap  = URLConnection.getFileNameMap();
                OutputStream output = connection.getOutputStream();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value==null) continue;

                    sb.append("--").append(boundary).append(changeRow);
                    sb.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"");

                    if (value instanceof File) {
                        File v = (File) value;
                        //发送文件
                        String contentType = fileNameMap.getContentTypeFor(v.getAbsolutePath());
                        sb.append("; filename=").append("\"").append(v.getName()).append("\"").append(changeRow)
                        .append("Content-Type: ").append(contentType).append(changeRow).append(changeRow);
                        output.write(sb.toString().getBytes());
                        sb.delete(0,sb.length());
                        InputStream is = new FileInputStream(v);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1){
                            output.write(buffer,0,len);
                        }
                        is.close();
                    } else {
                        sb.append(changeRow)
                        .append(changeRow)
                        .append(value.toString());
                    }
                    sb.append(changeRow);
                }
                sb.append("--").append(boundary).append("--").append(changeRow);
                byte[] data = sb.toString().getBytes();
                output.write(data);
            }
        } catch ( ConnectException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(COMMON_WARNING);
        }
        return commonGet(connection);
    }

    private static String genBodyByParams(Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            try {
                StringBuilder data = new StringBuilder(1024);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value==null) continue;

                    data.append(entry.getKey()).append("=");
                    data.append(URLEncoder.encode(value.toString(), "UTF-8"));
                    data.append("&");
                }
                if (data.length()>0) {
                    data.deleteCharAt(data.length() - 1);
                }
                String body = data.toString();
                return body;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new ServiceException(PARAM_ERROR);
            }
        }
        return null;
    }

    /**
     * get, set的统一设置
     * @return
     */
    private static HttpURLConnection commonSet(String method, String urlStr){
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new ServiceException(URL_ERROR);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(COMMON_WARNING);
        }
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new ServiceException(METHOD_ERROR);
        }
        connection.setDoInput(true);
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(20000);
        return connection;
    }
    /**
     * get, set的统一获取
     * @return
     */
    private static String commonGet(HttpURLConnection connection){
        try {
            if (200 != connection.getResponseCode()) {
                System.err.println("请求Url失败,ResponseCode"+connection.getResponseCode());
                throw new ServiceException(COMMON_WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(COMMON_WARNING);
        }
        InputStream in = null;
        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(COMMON_WARNING);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder(1024);
        String str = null;
        try {
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(COMMON_WARNING);
        }
        connection.disconnect();
        String result = sb.toString();
        return result;
    }

    public static Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
