package co.com.sofka.model.gateways;

import co.com.sofka.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryCrud<T, ID>{

    public Mono<T> findById(ID id);
    public Flux<T> findAll();

    Flux<T> findAllById(ID id);

    public Mono<T> create(T T);

    public Mono<T> update(T T, ID id);

    public Mono<Void> deleteById(ID id);




}