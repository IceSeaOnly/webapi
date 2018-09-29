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
@Data
@Entity
public class ApiResponse extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    /**
     * 序列号
     * */
    private Long sequenceId;
    /**
     * 版本号 [0,+∞)
     * */
    private Integer version;
    /**
     * 响应结果
     * */
    @Column(columnDefinition = "TEXT")
    private String responseBody;

    private String createIp;

    @Column(name = "is_deleted")
    public Boolean deleted;
}
