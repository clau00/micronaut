package com.learning.broker;

import com.learning.broker.data.InMemoryStore;
import com.learning.broker.persistence.model.SymbolEntity;
import com.learning.broker.persistence.jpa.SymbolsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/symbols")
public class SymbolsController {

    private final InMemoryStore inMEmoryStore;
    private final SymbolsRepository symbolsRepository;

    public SymbolsController(InMemoryStore inMEmoryStore, SymbolsRepository symbolsRepository) {
        this.inMEmoryStore = inMEmoryStore;
        this.symbolsRepository = symbolsRepository;
    }

    @Get
    public List<Symbol> getAll() {
        return new ArrayList<>(inMEmoryStore.getSymbols().values());
    }

    @Get("{value}")
    public Symbol getSymbolByValue(@PathVariable String value) {
        return inMEmoryStore.getSymbols().get(value);
    }

    @Get("/filter{?max,offset}")
    public List<Symbol> getSymbols(@QueryValue Optional<Integer> max, @QueryValue Optional<Integer> offset) {
        return inMEmoryStore.getSymbols().values()
                .stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(10))
                .collect(Collectors.toList());
    }

    @Get("/jpa")
    public List<SymbolEntity> allSymbolsViaJPA() {
        return symbolsRepository.findAll();
    }
}
