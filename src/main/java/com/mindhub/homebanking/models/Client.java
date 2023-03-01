package com.mindhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private final Set<Card> cards = new HashSet<>();
    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private final Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private final Set<ClientLoan> clientLoans = new HashSet<>();
    public Client(){}
    public Client(String firstName,String lastName,String email,String password){
        this.firstName = firstName;

        this.lastName = lastName;

        this.email = email;

        this.password = password;
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    public long getId(){
        return id;
    }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public Set <Account> getAccounts(){
        return accounts;
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public Set<Card> getCards() { return cards; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setEmail(String email){
        this.email = email;
    }
    @JsonIgnore
    public List<Loan> getLoans() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    }
    @Override
    public  String toString(){
        return firstName+" "+lastName+" "+email;
    }
}
