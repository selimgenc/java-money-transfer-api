package com.revolut.bank.domain.shared;

import com.revolut.bank.domain.account.Account;
import com.revolut.bank.domain.handling.DomainException;

import java.math.BigDecimal;
import java.util.Optional;

public class Validate {
    public static void isTrue(boolean expression, String message, String description, Object... values) throws DomainException {
        if (!expression) {
            throw new DomainException(message, String.format(description, values));
        }
    }

    public static void hasBalance(Account account, BigDecimal amount) throws DomainException {
        if(account.getCredit().compareTo(amount) < 0){
            throw new DomainException("account.transfer.notenoughbalance", "Not Enough balance");
        }
    }
    public static <T extends DomainEntity> void notNull(Optional<T> entity, String message, String description, Object... values) throws DomainException {
        if(!entity.isPresent()){
            throw new DomainException(message, String.format(description, values));
        }
    }
}
