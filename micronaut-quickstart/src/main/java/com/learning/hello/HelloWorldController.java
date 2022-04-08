package com.learning.hello;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/hello")
public class HelloWorldController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);

    private final MyService service;
    private final String helloFromConfig;
    private final HelloWorldTranslationConfig translationConfig;

    public HelloWorldController(MyService service, @Property(name = "hello.world.message") String helloFromConfig,
                                HelloWorldTranslationConfig translationConfig) {
        this.service = service;
        this.helloFromConfig = helloFromConfig;
        this.translationConfig = translationConfig;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String helloWorld() {
        LOG.debug("Called the hello world API");
        return service.helloWorldService();
    }

    @Get(value = "/config", produces = MediaType.TEXT_PLAIN)
    public String helloFromConfig() {
        LOG.debug("Return Hello From Config Message: {}", helloFromConfig);
        return helloFromConfig;
    }

    @Get(value = "/translation", produces = MediaType.APPLICATION_JSON)
    public HelloWorldTranslationConfig helloTranslation() {
        return translationConfig;
    }
}
