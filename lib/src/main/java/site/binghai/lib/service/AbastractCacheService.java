package site.binghai.lib.service;

import site.binghai.lib.def.CacheService;
import site.binghai.lib.utils.BaseBean;
import site.binghai.lib.utils.TimeTools;

public abstract class AbastractCacheService<T> extends BaseBean implements CacheService<T> {
    private long expiredSecs = 0l;
    private long lastCallTime = 0l;
    private T cache;


    public T get() {
        if (cache == null) {
            init();
        }
        if (expired()) {
            loadData();
        }

        return cache;
    }

    private boolean expired() {
        return now() - lastCallTime > expiredSecs;
    }


    private synchronized void init() {
        if (cache != null) return;
        expiredSecs = setExpiredSecs() * 1000;
        loadData();
    }

    private synchronized void loadData() {
        if (!expired()) {
            return;
        }
        cache = load();
        lastCallTime = now();
        logger.info("{} reload data at {}", this.getClass().getSimpleName(), TimeTools.now());
    }

    @Override
    public void immediateExpired() {
        lastCallTime = 0l;
    }
}
