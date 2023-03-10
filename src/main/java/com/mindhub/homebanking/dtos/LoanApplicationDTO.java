package com.mindhub.homebanking.dtos;
public class LoanApplicationDTO {
    private Long id;
    private int amount;
    private int payments;
    private String numberAccountDestini;
    public LoanApplicationDTO(long id, int amount, int payments, String numberAccountDestini) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestini = numberAccountDestini;
    }
    public Long getId() {
        return id;
    }
    public Integer getAmount() {
        return amount;
    }
    public Integer getPayments() {
        return payments;
    }
    public String getNumberAccountDestini() {
        return numberAccountDestini;
    }
}
