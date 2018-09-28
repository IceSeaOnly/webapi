package site.binghai.lib.def;


import site.binghai.lib.entity.UnifiedOrder;

/**
 * Created by IceSea on 2018/4/17.
 * GitHub: https://github.com/IceSeaOnly
 * 统一订单的具体子订单实体需实现该接口
 */
public interface UnifiedOrderMethods<T> {
    T moreInfo(UnifiedOrder order);
    T cancel(UnifiedOrder order);
}
