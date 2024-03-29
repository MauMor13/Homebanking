package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;
    private Boolean active = true;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    public Card() {}
    public Card(Client client, CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardHolder = client.getFirstName()+ " " + client.getLastName();

        this.type = type;

        this.color = color;

        this.number = number;

        this.cvv = cvv;

        this.fromDate = fromDate;

        this.thruDate = thruDate;
    }
    public Long getId() { return id; }
    public Client getClient() { return client; }
    public CardType getType() { return type; }
    public CardColor getColor() { return color; }
    public String getNumber() {return number; }
    public Integer getCvv() { return cvv; }
    public LocalDate getFromDate() {return fromDate; }
    public LocalDate getThruDate() { return thruDate; }
    public String getCardHolder() { return cardHolder; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
    public void setClient(Client client) { this.client = client; }
    public void setType(CardType type) { this.type = type; }
    public void setColor(CardColor color) { this.color = color; }
    public void setNumber(String number) { this.number = number; }
    public void setCvv(int cvv) { this.cvv = cvv; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    public void setThruDate(LocalDate thruDate) { this.thruDate = thruDate; }
}
