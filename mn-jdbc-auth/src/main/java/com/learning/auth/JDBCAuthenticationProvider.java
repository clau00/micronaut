package com.learning.auth;

import com.learning.persistence.UserEntity;
import com.learning.persistence.UserRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class JDBCAuthenticationProvider implements AuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(JDBCAuthenticationProvider.class);
    private final UserRepository userRepository;

    public JDBCAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable final HttpRequest<?> httpRequest,
                                                          final AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final String identity = authenticationRequest.getIdentity().toString();
            LOG.debug("User {} tries to login...", identity);

            Optional<UserEntity> maybeUser = userRepository.findByEmail(identity);
            if (maybeUser.isPresent()) {
                LOG.debug("User found: {}", maybeUser.get().getEmail());
                final String secret = authenticationRequest.getSecret().toString();
                if (maybeUser.get().getPassword().equals(secret)) {
                    // pass
                    LOG.debug("User logged in.");
                    emitter.onNext(AuthenticationResponse.success(identity));
                    emitter.onComplete();
                    return;
                } else {
                    LOG.debug("Wrong password provided for user: {}", identity);
                }
            } else {
                LOG.debug("No user found with email: {}", identity);
            }

            emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password!")));
        }, BackpressureStrategy.ERROR);
    }
}
