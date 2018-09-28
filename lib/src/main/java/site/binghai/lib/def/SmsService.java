package site.binghai.lib.def;

public interface SmsService {
    String sendVerifyCodeSms(String to, String code);
}
