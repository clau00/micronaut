package com.learning.broker.data;

import com.learning.broker.Symbol;
import com.learning.broker.wallet.DepositFiatMoney;
import com.learning.broker.wallet.Wallet;
import com.learning.broker.wallet.WithdrawFiatMoney;
import com.learning.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {

    public static final UUID ACCOUNT_ID = UUID.fromString("f4245629-83df-4ed8-90d9-7401045b5921");
    private final Map<UUID, WatchList> watchListPerAccount = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
        return watchListPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId) {
        watchListPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId))
                .orElse(new HashMap<>())
                .values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit) {
        return addAvailableInWallet(deposit.getAccountId(), deposit.getWalletId(), deposit.getSymbol(), deposit.getAmount());
    }

    public Wallet withdrawFromWallet(WithdrawFiatMoney withdraw) {
        return addAvailableInWallet(withdraw.getAccountId(), withdraw.getWalletId(), withdraw.getSymbol(), withdraw.getAmount());
    }

    private Wallet addAvailableInWallet(UUID accountId, UUID walletId, Symbol symbol, BigDecimal changeAmount) {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(accountId)
        ).orElse(
                new HashMap<>()
        );

        var oldWallet = Optional.ofNullable(
                wallets.get(walletId)
        ).orElse(
                new Wallet(ACCOUNT_ID, walletId, symbol, BigDecimal.ZERO, BigDecimal.ZERO)
        );

        var newWallet = oldWallet.addAvailable(changeAmount);

        // Update wallet in store
        wallets.put(newWallet.getWalletId(), newWallet);
        walletsPerAccount.put(newWallet.getAccountId(), wallets);

        return newWallet;
    }
}
