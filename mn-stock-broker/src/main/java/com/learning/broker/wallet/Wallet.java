package com.learning.broker.wallet;

import com.learning.broker.Symbol;
import com.learning.broker.api.RestApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet implements RestApiResponse {

    private UUID accountId;
    private UUID walletId;
    private Symbol symbol;
    private BigDecimal available;
    private BigDecimal locked;

    public Wallet(UUID accountID, UUID walletId, Symbol synmbol, BigDecimal available, BigDecimal locked) {
        this.accountId = accountID;
        this.walletId = walletId;
        this.symbol = synmbol;
        this.available = available;
        this.locked = locked;
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

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal getLocked() {
        return locked;
    }

    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(this.accountId, this.walletId, this.symbol, this.available.add(amountToAdd), this.locked);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "accountId=" + accountId +
                ", walletId=" + walletId +
                ", symbol=" + symbol +
                ", available=" + available +
                ", locked=" + locked +
                '}';
    }
}
