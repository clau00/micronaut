package com.learning;

import com.learning.persistence.UserEntity;
import com.learning.persistence.UserRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
public class TestDataProvider {

    private final UserRepository userRepository;

    public TestDataProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void init(StartupEvent event) {
        String email = "alice@example.com";
        if (userRepository.findByEmail(email).isEmpty()) {
            UserEntity alice = new UserEntity();
            alice.setEmail(email);
            alice.setPassword("secret");
            userRepository.save(alice);
        }
    }
}
