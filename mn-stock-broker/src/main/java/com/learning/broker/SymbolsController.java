package com.learning.broker;

import com.learning.broker.data.InMEmoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/symbols")
public class SymbolsController {

    private final InMEmoryStore inMEmoryStore;

    public SymbolsController(InMEmoryStore inMEmoryStore) {
        this.inMEmoryStore = inMEmoryStore;
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
}
