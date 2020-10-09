package model.api.transfer;

import lombok.Builder;

public class Deposit extends Transfer {

    @Builder
    public Deposit(double amount, String currency, String description ) {
        super(amount, currency, description);
    }

}


