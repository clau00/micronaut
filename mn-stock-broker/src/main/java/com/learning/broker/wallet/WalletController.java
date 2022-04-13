package com.learning.broker.wallet;

import com.learning.broker.data.InMemoryAccountStore;
import com.learning.broker.wallet.error.CustomError;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.Collection;
import java.util.List;

import static com.learning.broker.data.InMemoryAccountStore.ACCOUNT_ID;

@Controller("/account/wallets")
public class WalletController {

    public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR", "USD", "CHE", "GBP");

    private final InMemoryAccountStore store;

    public WalletController(InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get() {
        return store.getWallets(ACCOUNT_ID);
    }

    @Post(
            value = "/deposit",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public MutableHttpResponse<CustomError> depositFiatMoney(@Body DepositFiatMoney deposit) {
        //  Option 1: Custom HttpResponse
        if(!SUPPORTED_FIAT_CURRENCIES.contains(deposit.getSymbol().getValue())) {
            return HttpResponse.badRequest()
                    .body(new CustomError(
                            HttpStatus.BAD_REQUEST.getCode(),
                            "UNSUPPORTED_FIAT_CURRENCY",
                            String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES)
                    ));
        }

        return HttpResponse.ok();
    }

    @Post(
            value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public void withdrawFiatMoney(@Body WithdrawFiatMoney withdraw) {
        //  Option 2: Custom Error Processing
    }


}
