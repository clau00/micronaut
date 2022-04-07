package com.learning.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {

    private final HelloWorldService service;

    public HelloWorldController(HelloWorldService service) {
        this.service = service;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return service.helloWorldService();
    }
}
