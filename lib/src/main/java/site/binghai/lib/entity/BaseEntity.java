package site.binghai.lib.entity;

import site.binghai.lib.utils.TimeTools;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    private Long created;
    private String createdTime;

    public BaseEntity() {
        created = TimeTools.currentTS();
        createdTime = TimeTools.format(created);
    }


    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public abstract Long getId();
}

