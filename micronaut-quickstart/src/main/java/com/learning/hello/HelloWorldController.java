package com.learning.hello;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {

    private final MyService service;

    public HelloWorldController(MyService service) {
        this.service = service;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return service.helloWorldService();
    }
}
