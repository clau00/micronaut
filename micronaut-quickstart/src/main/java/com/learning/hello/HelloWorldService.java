package com.learning.hello;

import jakarta.inject.Singleton;

@Singleton
public class HelloWorldService {

    String helloWorldService() {
        return "Hello World from Service!";
    }
}
