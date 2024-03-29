package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private Boolean active = true;
    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER)
    private final Set<Transaction> transactions = new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Account(){}
    public Account(String number,LocalDateTime creationDate,double balance,AccountType accountType){
        this.number = number;

        this.creationDate = creationDate;

        this.balance = balance;

        this.accountType = accountType;
    }
    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public long getId(){
        return id;
    }
    public String getNumber(){
        return number;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public double getBalance(){
        return balance;
    }
    public Client getClient(){ return client; }
    public Set <Transaction> getTransactions(){
        return transactions;
    }
    public Boolean getActive() { return active; }
    public AccountType getAccountType() { return accountType; }
    public void setActive(Boolean active) { this.active = active; }
    public void setNumber(String number){
        this.number = number;
    }
    public void setCreationDate(LocalDateTime creationDate){
        this.creationDate=creationDate;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setClient(Client client){ this.client = client; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    @Override

    public  String toString(){
        return number+" "+creationDate+" "+balance;
    }

}
