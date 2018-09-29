package site.binghai.biz.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by IceSea on 2018/4/9.
 * GitHub: https://github.com/IceSeaOnly
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
@PropertySource("classpath:application.properties")
public class QiNiuAuthParams {
    private String ak;
    private String sk;
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}
