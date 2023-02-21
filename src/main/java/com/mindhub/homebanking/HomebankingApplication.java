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
import java.util.Random;

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
			Account vin001=new Account("vin001", LocalDateTime.now(),5000);
			Account vin002=new Account("vin002", LocalDateTime.now().plusDays(1),7500);
			Account vin003=new Account("vin003", LocalDateTime.now(),1200);
			Account vin004=new Account("vin004", LocalDateTime.now().plusDays(1),300);

			//utilizo método para generar transacciones aleatorias
			generateRandomTransactions(vin001,15);
			generateRandomTransactions(vin002,15);
			generateRandomTransactions(vin003,15);
			generateRandomTransactions(vin004,15);

			Loan loan1=new Loan("Mortgage",500000, List.of(12,24,36,48,60));
			Loan loan2=new Loan("Personal",100000, List.of(6,12,24));
			Loan loan3=new Loan("Automotive",300000, List.of(6,12,24,36));

			ClientLoan pres1=new ClientLoan( 400000, 60);
			ClientLoan pres2=new ClientLoan( 50000, 12);
			ClientLoan pres3=new ClientLoan( 100000, 24 );
			ClientLoan pres4=new ClientLoan(  200000, 36);

			Card card1=new Card(client, CardType.CREDIT, CardColor.GOLD,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card2=new Card(client, CardType.CREDIT, CardColor.SILVER,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card3=new Card(client, CardType.CREDIT, CardColor.TITANIUM,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card4=new Card(client, CardType.DEBIT, CardColor.GOLD,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card5=new Card(client, CardType.DEBIT, CardColor.SILVER,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
			Card card6=new Card(client, CardType.DEBIT, CardColor.TITANIUM,randomNumberCard(cardRepository) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));

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
	public static int returnCvvNumber(){
		int number;
		number= (int) (Math.random() * (999 - 100) + 100);
		return number;
	}
	public static String randomNumberCard(CardRepository cardRepository){
		String codString;
		Boolean cardBoolean;
		do {
			codString= randomString();
			cardBoolean= cardRepository.existsCardByNumber(codString);
		}while(cardBoolean);
		return codString;
	}
	public static String randomString(){
		int number1_1 = (int) (Math.random() * (5 - 4 + 1) + 4);
		int number1 = (int) ((Math.random() * (999 - 100) + 1) + 100);
		int number2 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		int number3 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		int number4 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
		return number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
	}
	public static void generateRandomTransactions(Account account, int cant){
		Random rand = new Random();
		TransactionType type;
		double amount;
		for (int i=0;i<cant;i++){
			String description = "Description "+ Integer.toString(i);
			amount = ((double) rand.nextInt(1000));
			if(rand.nextInt(2)==0){
				type=TransactionType.DEBIT;
				amount=((double) rand.nextInt(500))*-1;
			}else{
				type=TransactionType.CREDIT;
			}
			Transaction trans = new Transaction(type,amount,description,LocalDateTime.now());
			trans.setDate(LocalDateTime.now().plusMonths(rand.nextInt(9)).plusDays(rand.nextInt(10)));
			account.addTransaction(trans);
		}
	}
	public static void saveAccountsAndTransactions(Client client, AccountRepository accountRepository, TransactionRepository transactionRepository){
		for (Account account: client.getAccounts()){
			accountRepository.save(account);
			for(Transaction transaction: account.getTransactions()){
				transactionRepository.save(transaction);
			}
		}
	}
}