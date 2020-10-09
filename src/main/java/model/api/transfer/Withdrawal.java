package model.api.transfer;

import lombok.Builder;

public class Withdrawal extends Transfer {

    @Builder
    public Withdrawal(double amount, String currency, String description ) {
        super(amount, currency, description);
    }
}


