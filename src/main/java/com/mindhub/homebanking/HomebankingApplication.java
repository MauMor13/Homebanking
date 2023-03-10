package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static com.mindhub.homebanking.utils.Utilitis.*;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			//Creacion object
			Client client=new Client("Jack", "Bauer","melba@mindhub.com",passwordEncoder.encode("12345"));
			Client client2=new Client("Carl", "Ironman","ironman@mindhub.com",passwordEncoder.encode("67890"));
			Client admin=new Client("Admin","Karl","adminkarl@gmail.com",passwordEncoder.encode("123456"));
			Account vin001=new Account("VIN-001", LocalDateTime.now(),5000);
			Account vin002=new Account(GenereteNumber(accountRepository), LocalDateTime.now().plusDays(1),7500);
			Account vin003=new Account("VIN-003", LocalDateTime.now(),1200);
			Account vin004=new Account(GenereteNumber(accountRepository), LocalDateTime.now().plusDays(1),300);

			//utilizo método para generar transacciones aleatorias
			generateRandomTransactions(vin001,15);
			generateRandomTransactions(vin002,15);
			generateRandomTransactions(vin003,15);
			generateRandomTransactions(vin004,15);

			Loan loan1=new Loan("Mortgage",500000, List.of(12,24,36,48,60));
			Loan loan2=new Loan("Personal",100000, List.of(6,12,24));
			Loan loan3=new Loan("Automotive",300000, List.of(6,12,24,36));

			ClientLoan pres1=new ClientLoan(400000.0, 60);
			ClientLoan pres2=new ClientLoan(50000.0, 12);
			ClientLoan pres3=new ClientLoan(100000.0, 24 );
			ClientLoan pres4=new ClientLoan(200000.0, 36);

			Card card1=new Card(client, CREDIT, GOLD,randomNumberCard(cardRepository), returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card2=new Card(client, CREDIT, SILVER,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card3=new Card(client, CREDIT, TITANIUM,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card4=new Card(client, DEBIT, GOLD,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card5=new Card(client, DEBIT, SILVER,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card6=new Card(client, DEBIT, TITANIUM,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));

			//método add
			client.addAccount(vin001);
			client.addAccount(vin002);
			client2.addAccount(vin003);
			client2.addAccount(vin004);
			client.addClientLoan(pres1);
			loan1.addClientLoan(pres1);
			client.addClientLoan(pres2);
			loan2.addClientLoan(pres2);
			client2.addClientLoan(pres3);
			loan2.addClientLoan(pres3);
			client2.addClientLoan(pres4);
			loan3.addClientLoan(pres4);
			client.addCard(card1);
			client.addCard(card2);
			client.addCard(card3);
			client.addCard(card4);
			client.addCard(card5);
			client.addCard(card6);

			//método save de repositorio
			clientRepository.save(client);
			clientRepository.save(client2);
			clientRepository.save(admin);
			saveAccountsAndTransactions(client,accountRepository,transactionRepository);
			saveAccountsAndTransactions(client2,accountRepository,transactionRepository);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientLoanRepository.save(pres1);
			clientLoanRepository.save(pres2);
			clientLoanRepository.save(pres3);
			clientLoanRepository.save(pres4);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
			cardRepository.save(card5);
			cardRepository.save(card6);
		};

	}

}