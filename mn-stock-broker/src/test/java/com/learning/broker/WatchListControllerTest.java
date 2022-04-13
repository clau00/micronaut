package com.learning.broker;

import com.fasterxml.jackson.databind.JsonNode;
import com.learning.broker.data.InMemoryAccountStore;
import com.learning.broker.watchlist.WatchList;
import com.learning.broker.watchlist.WatchListController;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    @Client("/account/watchlist")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup() {
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void returnsEmptyWatchListForTestAccount() {
//        WatchList result = client.toBlocking().retrieve(HttpRequest.GET("/"), WatchList.class);
//        assertNull(result.getSymbols());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForTestAccount() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .collect(Collectors.toList())
        ));

        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("{\n" +
                                    "  \"symbols\" : [ {\n" +
                                        "    \"value\" : \"AAPL\"\n" +
                                    "  }, {\n" +
                                        "    \"value\" : \"GOOGL\"\n" +
                                    "  }, {\n" +
                                        "    \"value\" : \"MSFT\"\n" +
                                    "  } ]\n" +
                                "}",
                response.getBody().get().toPrettyString());
    }

    @Test
    void canUpdateWatchListForTestAccount() {
        var symbols = Stream.of("AAPL", "GOOGL", "MSFT")
                .map(Symbol::new)
                .collect(Collectors.toList());
        final var request = HttpRequest.PUT("/", new WatchList(symbols));
        final HttpResponse<Object> added = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols());
    }
}
