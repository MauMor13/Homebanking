package com.mindhub.homebanking.utils;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Random;
public class Utilitis {

    public static String GenereteNumber(AccountRepository accountRepository){
        String Number;
        boolean verifyNumber;
        do {
            Number = Number();
            verifyNumber = accountRepository.existsAccountByNumber(Number);
        }while(verifyNumber);
        return Number;
    }
    public static String Number(){
        int number1 = (int) (Math.random() * (99999999));
        return "VIN-"+number1;
    }
    public static int returnCvvNumber(){
        int number;
        number = (int) (Math.random() * (999 - 100) + 100);
        return number;
    }
    public static String randomNumberCard(CardRepository cardRepository){
        String codString;
        Boolean cardBoolean;
        do {
            codString = randomString();
            cardBoolean = cardRepository.existsCardByNumber(codString);
        }while(cardBoolean);
        return codString;
    }
    public static String randomString(){
        int number1_1 = (int) (Math.random() * (5 - 4) + 4);
        int number1 = (int) (Math.random() * (999 - 100) + 100);
        int number2 = (int) (Math.random() * (9999 - 1000) + 1000);
        int number3 = (int) (Math.random() * (9999 - 1000) + 1000);
        int number4 = (int) (Math.random() * (9999 - 1000) + 1000);
        return number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
    }
    public static void generateRandomTransactions(Account account, int cant){
        Random rand = new Random();
        TransactionType type;
        double amount;
        for (int i=0;i<cant;i++){
            String description = "Description "+ i;
            amount = rand.nextInt(1000);
            if(rand.nextBoolean()){
                type=TransactionType.DEBIT;
                amount=((double) rand.nextInt(500))*-1;
            }
            else{
                type=TransactionType.CREDIT;
            }
            Transaction trans = new Transaction(type,amount,description, LocalDateTime.now(),amount);
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
