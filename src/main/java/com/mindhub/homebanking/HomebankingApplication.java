package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client client=new Client("Jack", "Bauer","melba@mindhub.com");
			Account vin001=new Account("vin001", LocalDateTime.now(),5000);
			Account vin002=new Account("vin002", LocalDateTime.now().plusDays(1),7500);
			Client client2=new Client("Carl", "Ironman","ironman@mindhub.com");
			Account vin003=new Account("vin003", LocalDateTime.now(),1200);
			Account vin004=new Account("vin004", LocalDateTime.now().plusDays(1),300);
			Transaction tran1=new Transaction(TransactionType.DEBIT,-1000,"Withdrawal of money",LocalDateTime.now());
			Transaction tran2=new Transaction(TransactionType.CREDIT,10000,"Income of money",LocalDateTime.now());
			Transaction tran3=new Transaction(TransactionType.DEBIT,-1000,"Withdrawal of money",LocalDateTime.now());
			Transaction tran4=new Transaction(TransactionType.CREDIT,10000,"Income of money",LocalDateTime.now());

			client.addAccount(vin001);
			client.addAccount(vin002);
			client2.addAccount(vin003);
			client2.addAccount(vin004);
			vin001.addTransaction(tran1);
			vin002.addTransaction(tran2);
			vin003.addTransaction(tran3);
			vin004.addTransaction(tran4);

			clientRepository.save(client);
			accountRepository.save(vin001);
			accountRepository.save(vin002);
			clientRepository.save(client2);
			accountRepository.save(vin003);
			accountRepository.save(vin004);
			transactionRepository.save(tran1);
			transactionRepository.save(tran2);
			transactionRepository.save(tran3);
			transactionRepository.save(tran4);
		};
	}
}