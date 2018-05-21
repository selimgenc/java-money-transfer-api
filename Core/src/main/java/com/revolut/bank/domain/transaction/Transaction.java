package com.revolut.bank.domain.transaction;

import com.revolut.bank.domain.shared.DomainEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "Transaction.findAll",
                query = "Select t from Transaction t"),
        @NamedQuery(name = "Transaction.findByTransactionNo",
                query = "Select t from Transaction t where t.transactionNo = :transactionNo")
})
public class Transaction extends DomainEntity {
    @NotNull(message = "{transaction.transactionNo.wrong}")
    private String transactionNo;

    @Column(name = "FROM_ACCOUNT") //H2 throws exception for 'from'
    @NotNull(message = "{transaction.from.wrong}")
    private String from;

    @NotNull(message = "{transaction.to.wrong}")
    private String to;
    @NotNull(message = "{transaction.amount.wrong}")
    private BigDecimal amount;
    @NotNull(message = "{transaction.time.wrong}")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @NotNull(message = "{transaction.user.wrong}")
    private String user;
    private String location;

    public Transaction(@NotNull(message = "{transaction.transactionNo.wrong}") String transactionNo,
                       @NotNull(message = "{transaction.from.wrong}") String from,
                       @NotNull(message = "{transaction.to.wrong}") String  to,
                       @NotNull(message = "{transaction.amount.wrong}") BigDecimal amount,
                       @NotNull(message = "{transaction.time.wrong}") Date time,
                       @NotNull(message = "{transaction.user.wrong}") String user) {
        this.transactionNo = transactionNo;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.time = time;
        this.user=user;
    }
    public Transaction() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionNo='" + transactionNo + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                ", user='" + user + '\'' +
                ", location='" + location + '\'' +
                "} ";
    }

    //region Getters

    public String getTransactionNo() {
        return transactionNo;
    }
    public String  getFrom() {
        return from;
    }
    public String  getTo() {
        return to;
    }
    public Date getTime() {
        return time;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getUser() {
        return user;
    }
    public String getLocation() {
        return location;
    }
    //endregion


    public static final class Builder{
        private String transactionNo;
        private String from;
        private String to;
        private BigDecimal amount;
        private Date time;
        private String location = "UNKNOWN";
        private String user  = "UNKNOWN";


        public Builder(
                @NotNull(message = "{transaction.transactionNo.wrong}") String transactionNo,
                @NotNull(message = "{transaction.from.wrong}") String from,
                @NotNull(message = "{transaction.to.wrong}") String to,
                @NotNull(message = "{transaction.amount.wrong}") BigDecimal amaount,
                @NotNull(message = "{transaction.amount.wrong}") Date time) {
            this.transactionNo = transactionNo;
            this.from = from;
            this.to = to;
            this.amount = amaount;
            this.time= time;
        }

        public Builder user(String user){
            this.user= user;
            return this;
        }

        public Builder location(String location){
            this.location = location;
            return this;
        }


        public Transaction build(){
            Transaction transaction = new Transaction(transactionNo, from, to, amount, time, user);
            if(location !=null && !location.isEmpty()){
                transaction.location = location;
            }
            return transaction;
        }

    }
}
