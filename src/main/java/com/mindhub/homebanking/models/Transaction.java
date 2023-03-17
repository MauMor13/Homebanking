package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;
    private double amountAccount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    public Transaction(){}
    public Transaction(TransactionType type,double amount,String description,LocalDateTime date,double amountAccount){
        this.type = type;

        this.amount = amount;

        this.description = description;

        this.date = date;

        this.amountAccount = amountAccount;
    }
    public Long getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public Account getAccount(){ return account; }
    public double getAmountAccount() { return amountAccount; }
    public void setAccount(Account account){
        this.account = account;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setAmountAccount(double amountAccount) { this.amountAccount = amountAccount; }
}
