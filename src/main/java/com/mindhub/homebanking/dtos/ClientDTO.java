package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import java.util.Set;
import static java.util.stream.Collectors.toSet;


public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<ClientLoanDTO> loans;
    private Set <AccountDTO> accounts;
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts=client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());

        this.loans=client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(toSet());
    }
    public String getFirstName(){ return firstName; }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public long getId(){
        return id;
    }
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public Set<ClientLoanDTO> getLoans() { return loans; }
}
