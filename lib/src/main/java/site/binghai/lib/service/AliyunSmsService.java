package site.binghai.lib.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.lib.config.IceConfig;
import site.binghai.lib.def.SmsService;
import site.binghai.lib.utils.BaseBean;

@Service("aliyunSmsService")
public class AliyunSmsService extends BaseBean implements SmsService {
    @Autowired
    private IceConfig iceConfig;

    @Override
    public String sendVerifyCodeSms(String to, String code) {
        try {
            return send(to, code);
        } catch (Exception e) {
            logger.error("send code {} to {} error!", code, to, e);
        }
        return "FAIL";
    }

    public String send(String to, String code) throws Exception {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
        final String accessKeyId = iceConfig.getAliyunAccessKeyId();
        final String accessKeySecret = iceConfig.getAliyunAccessKeySecret();
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(to);
        request.setSignName("冰海软件");
        request.setTemplateCode("SMS_143713144");
        JSONObject params = newJSONObject();
        params.put("code", code);
        request.setTemplateParam(params.toJSONString());
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        String resp = toJsonObject(sendSmsResponse).toJSONString();
        logger.info("send sms to {},code:{},respoonse:{}", to, code, resp);
        return resp;
    }
}
