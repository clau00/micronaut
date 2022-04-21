package com.learning.broker.watchlist;

import com.learning.broker.Quote;
import com.learning.broker.data.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;

    public QuotesController(InMemoryStore store) {
        this.store = store;
    }

    @Get("/{symbol}")
    public HttpResponse<Quote> getQuote(@PathVariable String symbol) {
        Optional<Quote> quote = store.fetchQuote(symbol);
        return HttpResponse.ok(quote.get());
    }
}
