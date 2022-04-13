package com.learning.broker.wallet;

import com.learning.broker.Symbol;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class DepositFiatMoney {

    private UUID accountId;
    private UUID walletId;
    private Symbol symbol;
    private BigDecimal amount;

    public DepositFiatMoney() {
    }

    public DepositFiatMoney(UUID accountId, UUID walletId, Symbol symbol, BigDecimal amount) {
        this.accountId = accountId;
        this.walletId = walletId;
        this.symbol = symbol;
        this.amount = amount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
