package site.binghai.lib.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ice")
@PropertySource("classpath:application.properties")
@Data
public class IceConfig {
    private String appName;
    private String appRoot;

    private String miaodiSms_ACCOUNT_SID;
    private String miaodiSms_AUTH_TOKEN;

    private String aliyunAccessKeyId;
    private String aliyunAccessKeySecret;

    private static Map<String, String> setupParams = new HashMap<>();

    public static String getSetupParam(String key) {
        return setupParams.get(key);
    }

    public static void addSetupParam(String k, String v) {
        setupParams.put(k, v);
    }
}
