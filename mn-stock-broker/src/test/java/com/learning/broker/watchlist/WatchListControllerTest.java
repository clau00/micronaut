package com.learning.broker.watchlist;

import com.fasterxml.jackson.databind.JsonNode;
import com.learning.broker.Symbol;
import com.learning.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;
    public static final String ACCOUNT_WATCHLIST = "/account/watchlist";

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
    void unauthorizedAccessIsForbidden() {
        try {
            client.toBlocking().retrieve(ACCOUNT_WATCHLIST);
            fail("Should fail if no exception is thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        }
    }

    @Test
    void returnsEmptyWatchListForTestAccount() {
        final BearerAccessRefreshToken token = givenMyUserIsLoggedIn();

        var request = GET(ACCOUNT_WATCHLIST)
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

        var request = GET(ACCOUNT_WATCHLIST)
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

        var request = PUT(ACCOUNT_WATCHLIST, watchList)
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

        var request = DELETE(ACCOUNT_WATCHLIST)
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
