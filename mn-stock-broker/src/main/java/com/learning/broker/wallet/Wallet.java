package com.learning.broker.wallet;

import com.learning.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {

    private UUID accountID;
    private UUID walletId;
    private Symbol symbol;
    private BigDecimal available;
    private BigDecimal locked;

    public Wallet(UUID accountID, UUID walletId, Symbol synmbol, BigDecimal available, BigDecimal locked) {
        this.accountID = accountID;
        this.walletId = walletId;
        this.symbol = synmbol;
        this.available = available;
        this.locked = locked;
    }

    public UUID getAccountID() {
        return accountID;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal getLocked() {
        return locked;
    }
}
