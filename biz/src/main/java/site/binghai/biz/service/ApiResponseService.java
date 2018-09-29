package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.ApiResponse;
import site.binghai.biz.entity.WebApi;
import site.binghai.lib.service.BaseService;
import site.binghai.lib.utils.CompareUtils;
import site.binghai.lib.utils.GroovyEngineUtils;
import site.binghai.lib.utils.UrlUtil;

import java.util.List;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@Service
public class ApiResponseService extends BaseService<ApiResponse> {
    public ApiResponse lastestResponse(WebApi api) throws Exception {
        ApiResponse response = findById(api.getResponseId());
        if (response == null || response.getDeleted()) {
            throw new Exception("");
        }
        if (CompareUtils.inAny(api.getMethod(), "ALL", "GET")) {
            throw new Exception("method not support:" + api.getMethod());
        }
        return response;
    }

    public List<ApiResponse> findByApiId(Long apiId) {
        ApiResponse exp = new ApiResponse();
        exp.setSequenceId(apiId);
        exp.setDeleted(false);
        return sortQuery(exp, "id", true);
    }
}
