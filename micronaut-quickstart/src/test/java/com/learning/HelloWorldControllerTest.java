package com.learning;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
public class HelloWorldControllerTest {

    @Inject
    @Client("/")
    HttpClient client;


    @Test
    void helloWorldEndpointRespondsWithProperContent() {
        var response = client.toBlocking().retrieve("/hello");
        assertEquals("Hello World from Service!", response);
    }

    @Test
    void helloWorldEndpointRespondsWithProperStatusCodeAndContent() {
        var response = client.toBlocking().exchange("/hello", String.class);
        assertEquals("Hello World from Service!", response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void helloWorldEndpointReturnsMessageFromConfigFile() {
        var response = client.toBlocking().exchange("/hello/config", String.class);
        assertEquals("Hello from application.yml", response.getBody().get());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void helloFromTranslationEndpointReturnsMessageFromConfigFile() {
        var response = client.toBlocking().exchange("/hello/translation", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("{\"de\":\"Hallo Welt\",\"en\":\"Hello World\"}", response.getBody().get().toString());
    }
}
