package com.learning.broker.data;

import com.learning.broker.Symbol;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class InMEmoryStore {

    private static final Logger LOG = LoggerFactory.getLogger(InMEmoryStore.class);
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final String[] symbolsStr = {"TGA", "QTNT", "FALN", "SBNY", "NBIX", "ORG", "VTWV", "QVCA", "PTIE", "BWLD"};

    @PostConstruct
    public void initialize() {
        for(String str : symbolsStr) {
            var symbol = new Symbol(str);
            symbols.put(symbol.getValue(), symbol);
            LOG.debug("Added symbol {}", symbol);
        }
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
}
