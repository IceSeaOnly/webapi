package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.biz.entity.ApiResponse;
import site.binghai.biz.entity.WebApi;
import site.binghai.biz.service.ApiCallLogService;
import site.binghai.biz.service.ApiResponseService;
import site.binghai.biz.service.WebApiService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.utils.CompareUtils;
import site.binghai.lib.utils.GroovyEngineUtils;
import site.binghai.lib.utils.TokenGenerator;
import site.binghai.lib.utils.UrlUtil;

import java.security.PublicKey;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@RestController
public class WebApiController extends BaseController {
    @Autowired
    private WebApiService webApiService;
    @Autowired
    private ApiResponseService apiResponseService;
    @Autowired
    private ApiCallLogService apiCallLogService;

    private static final String API_NOT_EXIST = "api not exist.";
    private static final String AUTH_FAIL = "鉴权失败.";

    @PostMapping("createWebApi")
    public Object createWebApi(@RequestBody Map map) {
        logger.info("createWebApi");
        String responseBody = getString(map, "responseBody");
        String sourceIp = getSourceIp();

        if (hasEmptyString(responseBody)) {
            return fail("必须设定返回值!");
        }
        map.put("createIp", sourceIp);
        map.put("uuid", TokenGenerator.generate(16));

        WebApi webApi = webApiService.newInstance(map);
        if (hasEmptyString(webApi.getName(), webApi.getOriginalUrl(), webApi.getMethod())) {
            return fail("api名称、原始url、方法类型必传");
        }

        webApi.setNextVersion(1);
        webApi.setDeleted(false);
        webApi = webApiService.save(webApi);

        ApiResponse response = new ApiResponse();
        response.setName("[系统]首次创建");
        response.setCreateIp(sourceIp);
        response.setSequenceId(webApi.getId());
        response.setVersion(0);
        response.setResponseBody(responseBody);
        response.setDeleted(false);
        response = apiResponseService.save(response);

        webApi.setResponseId(response.getId());
        webApiService.update(webApi);

        apiCallLogService.log(webApi.getId(), response.getId(), null, null, "CREATE API", sourceIp);
        return success();
    }

    @PostMapping("createResponse")
    public Object createResponse(@RequestBody Map map, Integer bind) {
        Long webApiId = getLong(map, "apiId");
        String authCode = getString(map, "authCode");
        String name = getString(map, "name");
        String responseBody = getString(map, "responseBody");

        if (hasEmptyString(responseBody, name)) {
            return fail("必须设定返回值和备注!");
        }

        WebApi api = webApiService.findById(webApiId);
        if (api == null || api.getDeleted()) {
            return fail("api not found.");
        }

        if (!hasEmptyString(api.getAuthCode()) && !api.getAuthCode().equals(authCode)) {
            return fail(AUTH_FAIL);
        }

        ApiResponse response = new ApiResponse();
        response.setCreateIp(UrlUtil.getFullUrl(getServletRequest()));
        response.setSequenceId(webApiId);
        response.setVersion(api.getNextVersion());
        response.setResponseBody(responseBody);
        response.setName(name);

        apiResponseService.save(response);
        api.setNextVersion(api.getNextVersion() + 1);
        if (bind != null && bind == 1) {
            api.setResponseId(response.getId());
            apiCallLogService.log(api.getId(), api.getResponseId(), null, null, "updateCurrentResponse", getSourceIp());
        }
        webApiService.update(api);

        apiCallLogService.log(api.getId(), response.getId(), null, null, "CREATE RESPONSE",
            getSourceIp());
        return success();
    }

    @GetMapping("deleteApi")
    public Object delete(@RequestParam Integer type, @RequestParam Long id, String authCode) {
        if (type == 0) {
            WebApi api = webApiService.findById(id);
            if (api == null || api.getDeleted()) {
                return fail(API_NOT_EXIST);
            }
            if (!hasEmptyString(api.getAuthCode()) && !api.getAuthCode().equals(authCode)) {
                return fail(AUTH_FAIL);
            }
            api.setDeleted(true);
            webApiService.update(api);
            apiCallLogService.log(id, null, null, null, "DELETE API", getSourceIp());

        } else if (type == 1) {
            ApiResponse response = apiResponseService.findById(id);
            if (response == null || response.getDeleted()) {
                return fail(API_NOT_EXIST);
            }
            WebApi api = webApiService.findById(response.getSequenceId());
            if (api.getDeleted()) {
                return fail(API_NOT_EXIST);
            }
            if (!hasEmptyString(api.getAuthCode()) && !api.getAuthCode().equals(authCode)) {
                return fail(AUTH_FAIL);
            }
            response.setDeleted(true);
            apiResponseService.update(response);
            apiCallLogService.log(api.getId(), id, null, null, "DELETE RESPONSE", getSourceIp());
        }
        return success();
    }

    @GetMapping("updateCurrentResponse")
    public Object updateCurrentResponse(@RequestParam Long apiId, @RequestParam Long respId, String authCode) {
        WebApi api = webApiService.findById(apiId);
        if (api == null || api.getDeleted()) {
            return fail(API_NOT_EXIST);
        }
        if (!hasEmptyString(api.getAuthCode()) && !api.getAuthCode().equals(authCode)) {
            return fail(AUTH_FAIL);
        }
        ApiResponse response = apiResponseService.findById(respId);
        if (response == null || response.getDeleted()) {
            return fail(API_NOT_EXIST);
        }
        api.setResponseId(response.getId());

        apiCallLogService.log(apiId, respId, null, null, "updateCurrentResponse", getSourceIp());
        return success();
    }

    @GetMapping("/api/{uuid}")
    public Object getV2(@PathVariable(value = "uuid") String uuid) {
        Map ctx = UrlUtil.getRequestParams(getServletRequest());
        WebApi api = webApiService.findByUUID(uuid);
        if (api == null) {
            return fail(API_NOT_EXIST);
        }

        ApiResponse response = null;
        try {
            response = apiResponseService.lastestResponse(api);
        } catch (Exception e) {
            logger.error("call api failed!,uuid:{}", uuid, e);
            return fail(e.getMessage());
        }

        return buildResp(uuid, ctx, api, response, "GET API V2");
    }

    @PostMapping("/api")
    public Object post(@RequestBody Map map) {
        String uuid = getString(map, "uuid");
        WebApi api = webApiService.findByUUID(uuid);
        if (api == null) {
            return fail(API_NOT_EXIST);
        }
        ApiResponse response = null;
        try {
            response = apiResponseService.lastestResponse(api);
        } catch (Exception e) {
            logger.error("call api failed!,uuid:{}", uuid, e);
            return fail(e.getMessage());
        }

        return buildResp(uuid, map, api, response, "POST API");
    }

    @GetMapping("api")
    public Object get(@RequestParam String act) {
        String fullUrl = UrlUtil.getFullUrl(getServletRequest());
        int index = fullUrl.indexOf("?act=");
        fullUrl = fullUrl.substring(index + 5);
        WebApi api = webApiService.findBySourceUrl(fullUrl);
        if (api == null) {
            return fail(API_NOT_EXIST);
        }
        ApiResponse response = null;
        try {
            response = apiResponseService.lastestResponse(api);
        } catch (Exception e) {
            logger.error("call apiV1 failed!,full url:{}", fullUrl, e);
            return fail(e.getMessage());
        }

        return buildResp(api.getUuid(), null, api, response, "GET API V1");
    }

    @GetMapping("apiDetail")
    public Object apiDetail(@RequestParam Long apiId) {
        WebApi api = webApiService.findById(apiId);
        if (api == null || api.getDeleted()) {
            return fail(API_NOT_EXIST);
        }

        JSONObject ret = newJSONObject();
        api.setAuthCode(null);

        ret.put("api",api);
        ret.put("history",apiCallLogService.findRespHistoryByApiId(apiId));
        ret.put("resps",apiResponseService.findByApiId(apiId));

        return success(ret,null);
    }

    private Object buildResp(String uuid, Map ctx, WebApi api, ApiResponse response, String remark) {
        try {
            String resp = GroovyEngineUtils.instanceGroovyEngine(response.getResponseBody(), ctx);
            apiCallLogService.log(api.getId(), response.getId(), uuid, resp, remark, getSourceIp());
            return resp;
        } catch (Exception e) {
            logger.error("api call failed!,uuid:{},request:{}", uuid, UrlUtil.getFullUrl(getServletRequest()), e);
            return fail("call api error!" + e.getMessage());
        }
    }
}
