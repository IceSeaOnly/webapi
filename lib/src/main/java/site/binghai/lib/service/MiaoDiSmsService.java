package site.binghai.lib.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.lib.config.IceConfig;
import site.binghai.lib.def.SmsService;
import site.binghai.lib.utils.BaseBean;
import site.binghai.lib.utils.HttpUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("miaoDiSmsService")
public class MiaoDiSmsService extends BaseBean implements SmsService {
    @Autowired
    private IceConfig iceConfig;
    private static final String RESP_DATA_TYPE = "json";
    private static String operation = "/industrySMS/sendSMS";
    private static String smsContent = "【冰海科技】尊敬的用户，您的验证码为{%s}";
    public static final String BASE_URL = "https://api.miaodiyun.com/20150822";

    @Override
    public String sendVerifyCodeSms(String to, String code) {
        String tmpSmsContent = null;
        try {
            tmpSmsContent = URLEncoder.encode(String.format(smsContent, code), "UTF-8");
        } catch (Exception e) {

        }
        String url = BASE_URL + operation;
        String body = "accountSid=" + iceConfig.getMiaodiSms_ACCOUNT_SID() + "&to=" + to + "&smsContent=" + tmpSmsContent
                + createCommonParam();

        // 提交请求
        logger.info("sms verify url:{},body:{}", url, body);
        String result = HttpUtils.sendPost(url, null, body,"application/x-www-form-urlencoded");
        logger.info("send sms verify code to {},code :{},response:{}", to, code, result);
        return result;
    }

    public String createCommonParam() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String sig = DigestUtils.md5Hex(iceConfig.getMiaodiSms_ACCOUNT_SID() + iceConfig.getMiaodiSms_AUTH_TOKEN() + timestamp);

        return "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=" + RESP_DATA_TYPE;
    }
}
