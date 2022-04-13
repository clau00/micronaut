package com.learning.broker.watchlist;

import com.learning.broker.Symbol;

import java.util.ArrayList;
import java.util.List;

public class WatchList {

    private final List<Symbol> symbols;

    public WatchList() {
        this(new ArrayList<>());
    }

    public WatchList(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }
}
