package co.com.sofka.model.gateways;

import reactor.core.publisher.Mono;

public interface PersonRepository {

    Mono<String> findClientName(String clientId);
}
