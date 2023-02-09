package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
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
			Transaction tran5=new Transaction(TransactionType.DEBIT,-1000,"Withdrawal of money",LocalDateTime.now());
			Transaction tran6=new Transaction(TransactionType.DEBIT,-10000,"Income of money",LocalDateTime.now());
			Transaction tran7=new Transaction(TransactionType.CREDIT,1000,"Withdrawal of money",LocalDateTime.now());
			Transaction tran8=new Transaction(TransactionType.DEBIT,-10000,"Income of money",LocalDateTime.now());
			Loan loan1=new Loan("Mortgage",500000, List.of(12,24,36,48,60));
			Loan loan2=new Loan("Personal",100000, List.of(6,12,24));
			Loan loan3=new Loan("Automotive",300000, List.of(6,12,24,36));
			ClientLoan pres1=new ClientLoan( 400000, 60,client,loan1);
			ClientLoan pres2=new ClientLoan( 50000, 12 ,client,loan2);
			ClientLoan pres3=new ClientLoan( 100000, 24 ,client2,loan2);
			ClientLoan pres4=new ClientLoan(  200000, 36,client2,loan3);

			client.addAccount(vin001);
			client.addAccount(vin002);
			client2.addAccount(vin003);
			client2.addAccount(vin004);
			vin001.addTransaction(tran1);
			vin002.addTransaction(tran2);
			vin003.addTransaction(tran3);
			vin004.addTransaction(tran4);
			vin001.addTransaction(tran5);
			vin002.addTransaction(tran6);
			vin003.addTransaction(tran7);
			vin004.addTransaction(tran8);

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
			transactionRepository.save(tran5);
			transactionRepository.save(tran6);
			transactionRepository.save(tran7);
			transactionRepository.save(tran8);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientLoanRepository.save(pres1);
			clientLoanRepository.save(pres2);
			clientLoanRepository.save(pres3);
			clientLoanRepository.save(pres4);
		};
	}
}