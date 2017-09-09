package com.tool;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;

/**
 * Created by XCA on 2017/5/27.
 */
public class AliSmsServlet {


    /**
     * 阿里短信服务
     * @param phone
     * @param code
     * @param jsonStr
     * @param signName
     */
    public static void alisend(String phone, String code, String jsonStr, String signName){
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "nuVL8t3WgSu6fO7z", "xgsfz1Rt2AhB1xmIUbakffudGOpyuA");
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();
            //管理控制台中配置的短信签名（状态必须是验证通过）
            request.setSignName(signName);
            //管理控制台中配置的审核通过的短信模板的模板CODE（状态必须是验证通过）
            request.setTemplateCode(code);
//短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。
//例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456
            request.setParamString(jsonStr);
            //目标手机号，多个手机号可以逗号分隔
            request.setRecNum(phone);
//              request.setVersion(version);

            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            String requestId = httpResponse.getRequestId();
            System.err.println("requestId:"+requestId);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
