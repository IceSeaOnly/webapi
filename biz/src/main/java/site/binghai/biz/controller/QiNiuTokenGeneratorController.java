package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.entity.QiNiuAuthParams;
import site.binghai.biz.entity.QiNiuGeneratorLog;
import site.binghai.biz.service.QiNiuGeneratorLogService;
import site.binghai.lib.utils.UrlUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@RequestMapping("/")
@RestController
public class QiNiuTokenGeneratorController {
    @Autowired
    private QiNiuAuthParams authParams;
    @Autowired
    private QiNiuGeneratorLogService logService;


    @RequestMapping("qiniuToken")
    @CrossOrigin(origins = "*")
    public Object qiniuToken(HttpServletRequest request){
        String bucket = authParams.getBucket();
        Auth auth = Auth.create(authParams.getAk(), authParams.getSk());
        String upToken = auth.uploadToken(bucket);

        JSONObject obj = new JSONObject();
        obj.put("data",upToken);
        obj.put("status","SUCCESS");
        obj.put("msg","SUCCESS");

        QiNiuGeneratorLog log = new QiNiuGeneratorLog();
        log.setSourceIp(UrlUtil.getIpAdrress(request));
        log.setToken(upToken);
        logService.save(log);

        return obj;
    }

}
