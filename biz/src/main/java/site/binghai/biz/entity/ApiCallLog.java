package site.binghai.biz.entity;

import lombok.Data;
import site.binghai.lib.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
@Entity
@Data
public class ApiCallLog extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long apiId;
    private Long responseId;
    @Column(columnDefinition = "TEXT")
    private String requestCtx;
    @Column(columnDefinition = "TEXT")
    private String response;
    private String sourceIp;
    private String remark;
}
