package site.binghai.lib.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.binghai.lib.entity.SessionDataBundle;
import site.binghai.lib.interfaces.SessionPersistent;
import site.binghai.lib.utils.BaseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class BaseController extends BaseBean {

    protected String commonResp(String title, String info, String btn, String url, ModelMap map) {
        map.put("title", title);
        map.put("btn", btn);
        map.put("info", info);
        map.put("url", url);
        return "commonResp";
    }

    private static Map<Class, SessionPersistent> instanceHolder = new HashMap<>();

    /**
     * 从thread local获取网络上下文
     */
    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes;
        if (requestAttributes instanceof ServletRequestAttributes) {
            servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public HttpSession getSession() {
        return getServletRequest().getSession();
    }

    public <T extends SessionPersistent> T getSessionPersistent(Class<T> sp) {
        try {
            String tag = getInstanceTag(sp);
            return (T)getServletRequest().getSession().getAttribute(tag);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends SessionPersistent> String getInstanceTag(Class<T> sp)
        throws IllegalAccessException, InstantiationException {
        if (instanceHolder.get(sp) == null) {
            instanceHolder.put(sp, sp.newInstance());
        }
        return instanceHolder.get(sp).sessionTag();
    }

    public void setString2Session(String key, String value) {
        SessionDataBundle bundle = getSessionPersistent(SessionDataBundle.class);
        if (bundle == null) {
            bundle = new SessionDataBundle();
            persistent(bundle);
        }
        bundle.setAttribute(key, value);
    }

    public String getStringFromSession(String key) {
        SessionDataBundle bundle = getSessionPersistent(SessionDataBundle.class);
        return bundle == null ? null : bundle.getAttribute(key);
    }

    public <T extends SessionPersistent> void persistent(T entity) {
        getSession().setAttribute(entity.sessionTag(), entity);
    }

    public JSONObject fail(String err) {
        JSONObject object = new JSONObject();
        object.put("status", "FAIL");
        object.put("msg", err);
        return object;
    }

    public Object jsoupFail(String err, String callback) {
        if (StringUtils.isEmpty(callback)) {
            return fail(err);
        }
        return callback + "(" + fail(err).toJSONString() + ")";
    }

    public JSONObject success() {
        JSONObject object = new JSONObject();
        object.put("status", "SUCCESS");
        return object;
    }

    public JSONObject success(String msg) {
        JSONObject object = new JSONObject();
        object.put("status", "SUCCESS");
        object.put("msg", msg);
        return object;
    }

    public Object jsoupSuccess(Object data, String msg, String callBack) {
        if (StringUtils.isEmpty(callBack)) {
            return success(data, msg);
        }
        callBack = callBack == null ? "" : callBack;
        return callBack + "(" + success(data, msg).toJSONString() + ")";
    }

    public JSONObject success(Object data, String msg) {
        JSONObject object = new JSONObject();
        object.put("status", "SUCCESS");
        object.put("data", data);
        object.put("msg", msg);
        return object;
    }
}
