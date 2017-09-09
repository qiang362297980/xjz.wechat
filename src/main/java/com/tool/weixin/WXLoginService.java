package com.tool.weixin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tool.TemplateConfig;

/**
 * Created by XCA on 2017/6/1.
 */
public class WXLoginService {

    private static final Logger logger = LoggerFactory.getLogger(WXLoginService.class);

    public static final String WX_AUTH_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    //AppId
    public static final String WX_APP_ID = TemplateConfig.getValue("APPID");
    //AppSecret
    public static final String WX_APP_KEY = TemplateConfig.getValue("SECRET");

    public static UserInfoData checkLogin(String code){
        //获取授权 access_token
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append(WX_AUTH_LOGIN_URL).append("?appid=")
                .append(WX_APP_ID).append("&secret=")
                .append(WX_APP_KEY).append("&code=").append(code)
                .append("&grant_type=authorization_code");

        String loginRet = WXLoginService.get(loginUrl.toString());
        JSONObject grantObj = new JSONObject(loginRet);
        String errcode = grantObj.optString("errcode");
        if (!StringUtils.isEmpty(errcode))
        {
            logger.error("login weixin error"+loginRet);
            return null;
        }
        String openId = grantObj.optString("openid");
        if (StringUtils.isEmpty(openId))
        {
            logger.error("login weixin getOpenId error"+loginRet);
            return null;
        }

        String accessToken = grantObj.optString("access_token");
        String expiresIn = grantObj.optString("expires_in");
        String refreshToken = grantObj.optString("refresh_token");
        String scope = grantObj.optString("scope");

        //获取用户信息
        StringBuffer userUrl = new StringBuffer();
        userUrl.append(WX_USERINFO_URL).append("?access_token=").append(accessToken).append("&openid=").append(openId);
        String userRet = WXLoginService.get(userUrl.toString());
        JSONObject userObj = new JSONObject(userRet);

        UserInfoData userInfo = new UserInfoData();
        userInfo.setOpenId(openId);
        userInfo.setAuthToken(accessToken);
        userInfo.setAuthRefreshToken(refreshToken);
        userInfo.setScope(scope);
        userInfo.setExpiresIn(Integer.valueOf(expiresIn));
        String nickname = userObj.optString("nickname");
        String sex = userObj.optString("sex");
        String userImg = userObj.optString("headimgurl");
        String unionid = userObj.optString("unionid");
        userInfo.setName(nickname);
        userInfo.setIcon(userImg);
        userInfo.setGender(sex);
        userInfo.setLoginId(unionid);
        return userInfo;
    }

    public static String get(String url) {
        String body = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            logger.info("create httppost:" + url);
            HttpGet get = new HttpGet(url);
            get.addHeader("Accept-Charset","utf-8");
            HttpResponse response = sendRequest(httpClient, get);
            body = parseResponse(response);
        } catch (IOException e) {
            logger.error("send post request failed: {}", e.getMessage());
        }
        return body;
    }

    private static String paramsToString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        try{
            for (String key : params.keySet()) {
                sb.append(String.format("&%s=%s", key, URLEncoder.encode(params.get(key), StandardCharsets.UTF_8.toString())));
            }
        }catch(UnsupportedEncodingException e){
            logger.warn("{}: encode url parameters failed", e.getMessage());
        }
        return sb.length() > 0 ? "?".concat(sb.substring(1)) : "";
    }

    private static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost)
            throws ClientProtocolException, IOException {
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        return response;
    }

    private static String parseResponse(HttpResponse response) {
        logger.info("get response from http server..");
        HttpEntity entity = response.getEntity();

        logger.info("response status: " + response.getStatusLine());
        Charset charset = ContentType.getOrDefault(entity).getCharset();
        if (charset != null) {
            logger.info(charset.name());
        }

        String body = null;
        try {
            body = EntityUtils.toString(entity, "utf-8");
            logger.info("body " + body);
        } catch (IOException e) {
            logger.warn("{}: cannot parse the entity", e.getMessage());
        }

        return body;
    }
}
