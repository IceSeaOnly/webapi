package site.binghai.biz.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.biz.entity.RespEntity;
import site.binghai.biz.entity.WebApi;
import site.binghai.biz.service.RespEntityService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.utils.TimeTools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2018/09/29
 */
//@RestController
//@RequestMapping("fix")
public class FixController extends BaseController {
    @Autowired
    private RespEntityService respEntityService;
    @Autowired
    private WebApiController webApiController;

    @GetMapping("find")
    public Object find(@RequestParam Integer id){
        return respEntityService.findById(id);
    }

    @GetMapping("all")
    public Object all(){
        return respEntityService.findAll();
    }

    @GetMapping("fix")
    public Object fix(){

        for (RespEntity respEntity : respEntityService.findAll()) {
            WebApi api = new WebApi();
            api.setDeleted(false);
            api.setAuthCode(respEntity.getPassCode());
            api.setName(respEntity.getName());
            api.setCreated(TimeTools.dataTime2Timestamp(respEntity.getAddTime()));
            api.setCreatedTime(respEntity.getAddTime());
            api.setMethod("ALL");
            api.setOriginalUrl(respEntity.getRequest().substring("/api?act=".length()));
            JSONObject obj = toJsonObject(api);
            obj.put("responseBody",respEntity.getResp());
            webApiController.createWebApi(obj);
        }
        return "done";
    }
}
