package site.binghai.lib.service;

import site.binghai.lib.def.CacheService;
import site.binghai.lib.utils.BaseBean;
import site.binghai.lib.utils.TimeTools;

import java.util.Map;

public abstract class AbastractKVCacheService<K, V, P extends Map<K, V>> extends BaseBean implements CacheService<P> {
    private long expiredSecs = 0l;
    private long lastCallTime = 0l;
    private P cache;

    public V get(K key) {
        if (cache == null) {
            init();
        }
        if (expired()) {
            loadData();
        }

        return cache.get(key);
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
