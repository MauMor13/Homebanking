package com.mindhub.homebanking.dtos;
public class NewPayDTO {
    private final String number;
    private final Integer cvv;
    private final double amount;
    private final String description;
    public NewPayDTO(String number, String cvv, String amount, String description) {
        this.number = number;
        this.cvv = Integer.parseInt(cvv);
        this.amount = Integer.parseInt(amount);
        this.description = description;
    }
    public String getNumber() { return number; }
    public Integer getCvv() { return cvv; }
    public Double getAmount() { return amount; }
    public String getDescription() { return description; }
}
