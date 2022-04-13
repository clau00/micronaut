package com.learning.broker.watchlist;

import com.learning.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public class WatchListController {

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListController(InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

//    @Status(HttpStatus.NO_CONTENT)
    @Delete
    public HttpResponse<Void> delete() {
        store.deleteWatchList(ACCOUNT_ID);
        return HttpResponse.noContent();
    }
}
