package site.binghai.lib.inters;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class BaseInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute(getFilterTag(session)) != null) {
                return true;
            }
        }
        response.sendRedirect(getRedirectUrl(session));
        return false;
    }

    protected abstract String getRedirectUrl(HttpSession session);

    protected abstract String getFilterTag(HttpSession session);

}