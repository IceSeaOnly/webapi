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
public class WebApi extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String uuid;
    private Long responseId;


    /**
     * 原始url
     * */
    @Column(columnDefinition = "TEXT")
    private String originalUrl;

    /**
     * GET/POST/ALL
     * */
    private String method;
    /**
     * 修改时所用的密码
     * */
    private String authCode;

    private String createIp;

    private Integer nextVersion;

    @Column(name = "isDeleted")
    private Boolean deleted;
}
