package com.learning.hello;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Primary
@Singleton
public class HelloWorldService implements MyService{

    @Override
    public String helloWorldService() {
        return "Hello World from Service!";
    }
}
