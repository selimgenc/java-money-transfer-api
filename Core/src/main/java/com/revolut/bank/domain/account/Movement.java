package com.revolut.bank.domain.account;


import com.revolut.bank.domain.shared.DomainEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contains information about Account movements, also can be used to verify and construct credit statement of account
 */
@Entity
public class Movement extends DomainEntity {
    private BigDecimal amount;
    private Type type;

    public Movement(@NotNull BigDecimal amount, @NotNull Type type) {
        this.amount = amount;
        this.type = type;
    }

    public Movement() {
    }

    @Override
    public String toString() {
        return "Movement{" +
                "amount=" + amount +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(amount, movement.amount) &&
                type == movement.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type);
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public Type getType() {
        return type;
    }

    public static enum Type{
        DEPOSIT,
        WITHDRAW,
        INIT
    }
}
