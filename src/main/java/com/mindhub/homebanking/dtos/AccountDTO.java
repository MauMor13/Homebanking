package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import java.time.LocalDateTime;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private Set <TransactionDTO> transaction;

    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();
        this.transaction=account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());

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
    public long getId(){
        return id;
    }
    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }
}