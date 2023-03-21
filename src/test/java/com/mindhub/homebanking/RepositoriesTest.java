package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import java.util.List;
import java.util.stream.Collectors;
@SpringBootTest
//@DataJpaTest
public class RepositoriesTest {
    @Autowired
    AccountService accountServices;
    @Autowired
    CardService cardServices;
    @Autowired
    ClientService clientServices;
    @Autowired
    LoanService loanServices;
    @Autowired
    TransactionService transactionServices;

    @Test
    public void startsWithVIN() {
        List<Account> accounts = accountServices.findAll();
        for (Account account : accounts) {
            assertThat(account.getNumber(), startsWith("VIN"));
        }
    }

    @Test
    public void accountHasClient() {
        List<Account> accounts = accountServices.findAll();
        for (Account account : accounts) {
            assertThat(account.getClient(), is(not(nullValue())));
        }
    }

    @Test
    public void cardHaveType() {
        List<Card> allCards = cardServices.findAll();
        assertThat(allCards, hasItem(hasProperty("type", isA(Enum.class))));
    }

    @Test
    public void lengthCVV() {
        List<Card> allCards = cardServices.findAll();
        for (Card card : allCards) {
            assertThat(String.valueOf(card.getCvv()), hasLength(3));
        }
    }

    @Test
    public void clientHasAtLeastOneAccount() {
        List<Client> clients = clientServices.findAll().stream().filter(client -> !client.getEmail().equals("admin@mindhub.com")).collect(Collectors.toList());
        for (Client client : clients) {
            assertTrue("All clients has at least one account", !client.getAccounts().isEmpty());
        }
    }

    @Test
    public void adminHasNoAccount() {
        Client admin = clientServices.findByEmail("admin@mindhub.com");
        assertThat(admin.getAccounts(), is(empty()));
    }

    @Test
    public void existLoans() {
        List<Loan> loans = loanServices.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanServices.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void allTransactionsHaveAccount() {
        List<Transaction> transactions = transactionServices.findAll();
        assertThat(transactions, hasItem(hasProperty("account", is(notNullValue()))));
    }

    @Test
    public void transactionHaveType() {
        List<Transaction> transactions = transactionServices.findAll();
        for (Transaction transaction : transactions) {
            assertThat(transaction.getDescription(), not(emptyString()));
        }
    }
}