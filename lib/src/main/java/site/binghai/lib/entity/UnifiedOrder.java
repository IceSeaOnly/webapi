package site.binghai.lib.entity;

import lombok.Data;
import site.binghai.lib.enums.OrderStatusEnum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by IceSea on 2018/4/4.
 * GitHub: https://github.com/IceSeaOnly
 * 统一订单
 */
@Entity
@Data
public class UnifiedOrder extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    /**
     * 支付业务代码 {@link site.binghai.lib.enums.PayBizEnum}
     */
    private Integer appCode;
    private String title;
    private String remark;
    private String openId;
    private Long userId;
    private String userName;
    private String userPhone;

    public UnifiedOrder() {
        orderId = UUID.randomUUID().toString().substring(9);
        status = OrderStatusEnum.CREATED.getCode();
    }

    /**
     * 订单状态 {@link OrderStatusEnum}
     */
    private Integer status;
    /**
     * 订单原价
     */
    private Integer originalPrice;
    /**
     * 订单应付
     */
    private Integer shouldPay;
    /**
     * 使用的优惠券id，未使用则为-1
     */
    private Long couponId;

    @Override
    public Long getId() {
        return id;
    }

    public OrderStatusEnum orderState(){
        return OrderStatusEnum.valueOf(status);
    }

    public double originalDoublePrice(){
        return originalPrice/100.0;
    }

    public double shouldPayDouble(){
        return shouldPay/100.0;
    }
}
