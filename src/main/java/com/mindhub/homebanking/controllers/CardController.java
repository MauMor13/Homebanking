package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.NewPayDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static com.mindhub.homebanking.utils.Utilitis.randomNumberCard;
import static com.mindhub.homebanking.utils.Utilitis.returnCvvNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
@RequestMapping("/api")
@RestController
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        return clientService.findByEmail(authentication.getName()).getCardsActive().stream().map(CardDTO::new).collect(toList());
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(
            Authentication authentication,
            @RequestParam CardType type,
            @RequestParam CardColor color){
        Client clientauth=clientService.findByEmail(authentication.getName());
        Set<Card> cards=clientauth.getCardsActive().stream().filter(card -> card.getType()==type).collect(toSet());
        if(cards.size()>=3){
            return new ResponseEntity<>("You have already reached the limit of 3 "+type+" cards, you cannot be given another one", HttpStatus.BAD_REQUEST);
        }
        if (cards.stream().anyMatch(card -> card.getColor()==color&&card.getType()==type)){
            return new ResponseEntity<>("You have already the same card", HttpStatus.BAD_REQUEST);
        }
        Card card = new Card(clientauth,type,color,randomNumberCard(cardService) , returnCvvNumber(), LocalDate.now(),LocalDate.now().plusYears(5));
        clientauth.addCard(card);
        cardService.save(card);
        return new ResponseEntity<>("Card created successfully",HttpStatus.CREATED);
    }
    @PatchMapping ("/card-delete")//nuevo servlet-
    public ResponseEntity<Object> deleteCard(
            Authentication authentication,
            @RequestParam String numberCard){
        Client clientauth=clientService.findByEmail(authentication.getName());
        if (!cardService.existsCardByNumber(numberCard))
            return new ResponseEntity<>("The card number does not exist",HttpStatus.BAD_REQUEST);
        if (clientauth.getCardsActive().stream().noneMatch(card -> card.getNumber().equals(numberCard)))
            return new ResponseEntity<>("The card does not belong to the customer",HttpStatus.BAD_REQUEST);

        Card card = cardService.findByNumber(numberCard);
        card.setActive(false);
        cardService.save(card);
        return new ResponseEntity<>("Card removed successfully",HttpStatus.ACCEPTED);
    }
    @Transactional
    @PostMapping("/pay")
    public ResponseEntity <Object> payCards(
            @RequestBody(required = false) NewPayDTO newPayDTO){
        Card card = cardService.findByNumber(newPayDTO.getNumber());
        if (newPayDTO.getNumber().isEmpty())
            return new ResponseEntity<>("The card number is missing",HttpStatus.BAD_REQUEST);
        if (newPayDTO.getAmount() == null && newPayDTO.getAmount()>0)
            return new ResponseEntity<>("The amount to be paid is missing",HttpStatus.BAD_REQUEST);
        if (newPayDTO.getCvv() == null)
            return new ResponseEntity<>("The card cvv is missing",HttpStatus.BAD_REQUEST);
        if (newPayDTO.getDescription().isEmpty())
            return new ResponseEntity<>("The description is missing",HttpStatus.BAD_REQUEST);
        if (card == null)
            return new ResponseEntity<>("Card not found",HttpStatus.BAD_REQUEST);
        if (!card.getActive())
            return new ResponseEntity<>("The card is not valid",HttpStatus.BAD_REQUEST);
        if (card.getType() != DEBIT)
            return new ResponseEntity<>("The card is not debit",HttpStatus.BAD_REQUEST);
        if (card.getThruDate().isBefore(LocalDate.now()))
            return new ResponseEntity<>("The card has expired",HttpStatus.BAD_REQUEST);
        Account account = card.getClient().getAccounts().stream().max(Comparator.comparing(Account::getBalance)).get();
        if (account == null)
            return new ResponseEntity<>("There is no associated account",HttpStatus.BAD_REQUEST);
        if (!account.getActive())
            return new ResponseEntity<>("The associated account is inactive",HttpStatus.BAD_REQUEST);
        if (account.getBalance()<newPayDTO.getAmount())
            return new ResponseEntity<>("Your account balance does not cover the payment",HttpStatus.BAD_REQUEST);
        if (!Objects.equals(newPayDTO.getNumber(), card.getNumber()) ||!Objects.equals(newPayDTO.getCvv(), card.getCvv()))
            return new ResponseEntity<>("the data entered is incorrect",HttpStatus.BAD_REQUEST);

        Transaction transaction = new Transaction(TransactionType.DEBIT,-newPayDTO.getAmount(), newPayDTO.getDescription(), LocalDateTime.now(),account.getBalance() - newPayDTO.getAmount());
        account.setBalance(account.getBalance() - newPayDTO.getAmount());
        account.addTransaction(transaction);
        transactionService.save(transaction);
        accountService.save(account);
        return new ResponseEntity<>("Payment made",HttpStatus.ACCEPTED);
    }
}
