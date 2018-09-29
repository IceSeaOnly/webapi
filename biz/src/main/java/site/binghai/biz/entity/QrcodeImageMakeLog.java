package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@Entity
@Data
public class QrcodeImageMakeLog extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String ctx;
    private String sourceUrl;
    private String sourceIp;
}
