package site.binghai.biz.service;

import org.springframework.stereotype.Service;
import site.binghai.biz.entity.ApiCallLog;
import site.binghai.lib.service.BaseService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@Service
public class ApiCallLogService extends BaseService<ApiCallLog> {

    @Transactional
    public void log(Long apiId, Long respId, String input, String output, String remark, String ip) {
        ApiCallLog log = new ApiCallLog();
        log.setApiId(apiId);
        log.setResponseId(respId);
        log.setRequestCtx(input);
        log.setResponse(output);
        log.setSourceIp(ip);
        log.setRemark(remark);

        save(log);
    }

    public List<ApiCallLog> findRespHistoryByApiId(Long apiId) {
        ApiCallLog log = new ApiCallLog();
        log.setApiId(apiId);
        log.setRemark("updateCurrentResponse");

        return sortQuery(log,"id",true);
    }
}
