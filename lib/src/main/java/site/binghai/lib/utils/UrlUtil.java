package site.binghai.lib.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/5.
 */
public class UrlUtil {
    public static String getFullUrl(HttpServletRequest request) {
        String strBackUrl = "http://" + request.getServerName() //服务器地址
            + ":"
            + request.getServerPort()           //端口号
            + request.getContextPath()      //项目名称
            + request.getServletPath()      //请求页面或其他地址
            + "?" + (request.getQueryString()); //参数
        return strBackUrl;
    }

    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        String query = request.getQueryString();
        if (StringUtils.isBlank(query)) {
            return params;
        }
        String[] kvs = query.split("&");
        for (String kv : kvs) {
            String[] pair = kv.split("=");
            params.put(pair[0], pair[1]);
        }
        return params;
    }

    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }
}
