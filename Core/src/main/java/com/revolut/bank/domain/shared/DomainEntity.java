package com.revolut.bank.domain.shared;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public abstract class DomainEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    private Boolean frozen = Boolean.FALSE;

    private String createdBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntity)) return false;
        DomainEntity that = (DomainEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
