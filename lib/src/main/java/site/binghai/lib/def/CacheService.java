package site.binghai.lib.def;

public interface CacheService<T> {
    T load();
    void immediateExpired();
    long setExpiredSecs();
}
