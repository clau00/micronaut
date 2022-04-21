package com.learning.broker.persistence.jpa;

import com.learning.broker.persistence.model.QuoteEntity;
import com.learning.broker.persistence.model.SymbolEntity;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private final SymbolsRepository symbolsRepository;
    private final QuotesRepository quotesRepository;

    public TestDataProvider(SymbolsRepository symbolsRepository, QuotesRepository quotesRepository) {
        this.symbolsRepository = symbolsRepository;
        this.quotesRepository = quotesRepository;
    }

    @EventListener
    public void init(StartupEvent event) {
        if (symbolsRepository.findAll().isEmpty()) {
            LOG.info("Adding test data as empty database was found!!!");
            Stream.of("AAPL", "AMZN", "FB", "TSLA")
                    .map(SymbolEntity::new)
                    .forEach(symbolsRepository::save);
        }

        if (quotesRepository.findAll().isEmpty()) {
            LOG.info("Adding test data as empty database was found!!!");
            symbolsRepository.findAll().forEach(symbol -> {
                var quote = new QuoteEntity();
                quote.setSymbol(symbol);
                quote.setAsk(randomValue());
                quote.setBid(randomValue());
                quote.setVolume(randomValue());
                quote.setLastPrice(randomValue());
                quotesRepository.save(quote);
            });
        }
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(RANDOM.nextDouble(1, 100));
    }
}
