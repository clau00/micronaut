package com.learning.broker;

import com.learning.broker.data.InMemoryStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class SymbolsControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMEmoryStore;

    @BeforeEach
    void init() {
        inMEmoryStore.initialize();
    }

    @Test
    void symbolsEndpointReturnsListOfSymbol() {
        var response = client.toBlocking().exchange("/", List.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsTheCorrectSymbol() {
        var testSymbol = new Symbol("TEST");
        inMEmoryStore.getSymbols().put(testSymbol.getValue(), testSymbol);

        var response = client.toBlocking().exchange("/" + testSymbol.getValue(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(testSymbol.getValue(), response.getBody().get().getValue());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolTakingQueryParametersIntoAccount() {
        var max10 = client.toBlocking().exchange("/filter?max=10", JsonNode.class);
        assertEquals(HttpStatus.OK, max10.getStatus());
        LOG.debug("Max: 10: {}", max10.getBody().get().size());
        assertEquals(10, max10.getBody().get().size());

        var offset7 = client.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        assertEquals(HttpStatus.OK, offset7.getStatus());
        LOG.debug("Offset: 7: {}", offset7.getBody().get().size());
        assertEquals(4, offset7.getBody().get().size());

        var max2Offset7 = client.toBlocking().exchange("/filter?max=2&offset=7", JsonNode.class);
        assertEquals(HttpStatus.OK, max2Offset7.getStatus());
        LOG.debug("Max 2, Offset: 7: {}", max2Offset7.getBody().get().size());
        assertEquals(2, max2Offset7.getBody().get().size());
    }

}
