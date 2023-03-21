package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Set<ClientLoanDTO> loans;
    private final Set<AccountDTO> accounts;
    private final Set<CardDTO> cards ;
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccountsActive().stream().map(account -> new AccountDTO(account)).collect(toSet());

        this.loans = client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(toSet());

        this.cards = client.getCardsActive().stream().map(card -> new CardDTO(card)).collect(toSet());
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
    public Set<CardDTO> getCards() { return cards; }
}
