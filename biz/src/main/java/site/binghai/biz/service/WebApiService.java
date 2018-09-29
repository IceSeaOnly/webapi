package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.WebApi;
import site.binghai.lib.service.BaseService;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@Service
public class WebApiService extends BaseService<WebApi> {

    public WebApi findByUUID(String uuid) {
        WebApi exp = new WebApi();
        exp.setDeleted(false);
        exp.setUuid(uuid);
        return queryOne(exp);
    }
}
