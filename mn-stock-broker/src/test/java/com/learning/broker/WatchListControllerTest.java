package com.learning.broker;

import com.fasterxml.jackson.databind.JsonNode;
import com.learning.broker.data.InMemoryAccountStore;
import com.learning.broker.watchlist.WatchList;
import com.learning.broker.watchlist.WatchListController;
import io.micronaut.http.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
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

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup() {
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void returnsEmptyWatchListForTestAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();

        var request = GET("/account/watchlist")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        WatchList result = client.toBlocking().retrieve(request, WatchList.class);
        assertTrue(result.getSymbols().isEmpty());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnsWatchListForTestAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();
        givenWatchListForAccountExists();

        var request = GET("/account/watchlist")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        var response = client.toBlocking().exchange(request, JsonNode.class);
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
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();

        var symbols = Stream.of("AAPL", "GOOGL", "MSFT")
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        var request = PUT("/account/watchlist", watchList)
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        final HttpResponse<Object> added = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, added.getStatus());
        assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols());
    }

    @Test
    void canDeleteWatchListForTestAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();

        givenWatchListForAccountExists();
        assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());

        var request = DELETE("/account/watchlist")
                .accept(MediaType.APPLICATION_JSON)
                .bearerAuth(token.getAccessToken());
        var deleted = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatus());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    private BearerAccessRefreshToken givenMyUserIsLoggedIn() {
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("my-user", "secret");
        var login = HttpRequest.POST("/login", credentials);
        var response = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);
        assertEquals(HttpStatus.OK, response.getStatus());

        final BearerAccessRefreshToken token = response.body();
        assertNotNull(token);

        assertEquals("my-user", response.body().getUsername());
        LOG.debug("Login Bearer Token: {} expires in {}", response.body().getAccessToken(), response.body().getExpiresIn());
        return token;
    }

    private void givenWatchListForAccountExists() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .collect(Collectors.toList())
        ));
    }
}
