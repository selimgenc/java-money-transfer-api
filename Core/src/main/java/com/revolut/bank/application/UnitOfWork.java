package com.revolut.bank.application;

import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.DomainEntity;

import java.io.Serializable;

public interface UnitOfWork{
    void registerNew(DomainEntity entity);
    void registerModified(DomainEntity entity);
    void registerDeleted(DomainEntity entity);
    void beginTransaction();
    void commit() throws DomainException;

    enum Register{
        NEW,
        UPDATE,
        DELETE
    }

}
