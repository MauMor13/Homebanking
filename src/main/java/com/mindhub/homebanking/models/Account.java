package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native",strategy="native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clientId;

    public Account(){}
    public Account(String number,LocalDateTime creationDate,double balance){
        this.number=number;
        this.creationDate=creationDate;
        this.balance=balance;
    }
    public String getNumber(){
        return number;
    }
    public void setNumber(String number){
        this.number=number;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate){
        this.creationDate=creationDate;
    }
    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance=balance;
    }
    public long getId(){
        return id;
    }
    public Client getClientId(){ return clientId; }
    public void setClientId(Client clientId){
        this.clientId=clientId;
    }
    @Override

    public  String toString(){
        return number+" "+creationDate+" "+balance;
    }
}
