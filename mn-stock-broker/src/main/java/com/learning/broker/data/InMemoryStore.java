package com.learning.broker.data;

import com.learning.broker.Quote;
import com.learning.broker.Symbol;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class InMemoryStore {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Map<String, Quote> cachedQuotes = new HashMap<>();
    private final String[] symbolsStr = {"TGA", "QTNT", "FALN", "SBNY", "NBIX", "ORG", "VTWV", "QVCA", "PTIE", "BWLD"};
    private ThreadLocalRandom current = ThreadLocalRandom.current();

    @PostConstruct
    public void initialize() {
        for(String str : symbolsStr) {
            var symbol = new Symbol(str);
            symbols.put(symbol.getValue(), symbol);
            cachedQuotes.put(symbol.getValue(), generateQuote(symbol.getValue()));
            LOG.debug("Added symbol {}", symbol);
        }
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public Optional<Quote> fetchQuote(String symbol) {
        return Optional.ofNullable(cachedQuotes.get(symbol));
    }

    private Quote generateQuote(String symbol) {
        return Quote.builder().symbol(new Symbol(symbol))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble(1, 100));
    }

    public void update(Quote quote) {
        cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }
}
