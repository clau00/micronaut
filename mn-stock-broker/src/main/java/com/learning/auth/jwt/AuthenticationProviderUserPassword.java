package com.learning.auth.jwt;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable final HttpRequest<?> httpRequest,
                                                          final AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final Object identity = authenticationRequest.getIdentity();
            final Object secret = authenticationRequest.getSecret();

            LOG.debug("User {} tries to login...", identity);

            if (identity.equals("my-user") && secret.equals("secret")) {
                // pass
                emitter.onNext(AuthenticationResponse.success((String) identity));
                emitter.onComplete();
                return;
            }
            emitter.onError(AuthenticationResponse.exception());
        }, BackpressureStrategy.ERROR);
    }
}
