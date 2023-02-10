package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String name;
    private int maxAmount;
    @ElementCollection
    @Column (name = "payment")
    private List<Integer> payments=new ArrayList<>();
    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans=new HashSet<>();

    public Loan(){}
    public Loan(String name, int maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Set<ClientLoan> getClientloans() {
        return clientLoans;
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
       public List<Client> getClients() {
            return clientLoans.stream().map(cl -> cl.getClient()).collect(toList());
       }
}
