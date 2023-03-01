package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private final long id;
    private final int amount;
    private final int payments;
    private final Long Loans_id;
    private final String name;

    public ClientLoanDTO(ClientLoan clientLoans){
        this.id = clientLoans.getId();

        this.amount = clientLoans.getAmount();

        this.payments = clientLoans.getPayments();

        this.Loans_id = clientLoans.getLoan().getId();

        this.name = clientLoans.getLoan().getName();
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Long getLoans_id() {
        return Loans_id;
    }

    public String getName() {
        return name;
    }
}
