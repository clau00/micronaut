package com.learning.hello;

import jakarta.inject.Singleton;

@Singleton
public class SecondHelloWorldService implements MyService{

    @Override
    public String helloWorldService() {
        return "Hello World from Second Service!";
    }
}
