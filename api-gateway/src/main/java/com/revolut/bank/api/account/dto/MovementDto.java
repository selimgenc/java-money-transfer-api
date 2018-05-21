package com.revolut.bank.api.account.dto;

import com.revolut.bank.domain.account.Movement;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovementDto implements Serializable{
    private BigDecimal amount;
    private String type;

    public static MovementDto buildFromEntity(Movement movement){
        MovementDto dto = new MovementDto();
        dto.setAmount(movement.getAmount());
        dto.setType(String.valueOf(movement.getType()));
        return dto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
