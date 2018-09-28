package site.binghai.lib.entity;

import site.binghai.lib.interfaces.SessionPersistent;

import java.util.HashMap;
import java.util.Map;

public class SessionDataBundle implements SessionPersistent {
    private static final String BACK_URL = "BACK_URL";


    private Map<String, String> attrs;

    public SessionDataBundle() {
        this.attrs = new HashMap<>();
    }

    public String getAttribute(String key) {
        return attrs.get(key);
    }

    public void setAttribute(String key, String value) {
        attrs.put(key, value);
    }

    @Override
    public String sessionTag() {
        return "_Session_Data_Bundle_";
    }
}
