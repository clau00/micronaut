package com.learning.broker;

import com.learning.broker.data.InMEmoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.List;

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
}
