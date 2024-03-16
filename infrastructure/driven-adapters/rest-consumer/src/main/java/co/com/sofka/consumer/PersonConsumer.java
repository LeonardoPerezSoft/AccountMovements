package co.com.sofka.consumer;

import co.com.sofka.model.gateways.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class PersonConsumer implements PersonRepository {

    private final WebClient webClient;

    @Override
    public Mono<String> findClientName(String clientId) {
        return webClient
                .get()
                .uri("/clientes/name/{clientId}", clientId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
