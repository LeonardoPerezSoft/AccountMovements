package co.com.sofka.r2dbc.repositories;

import co.com.sofka.r2dbc.entities.AccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;


public interface AccountAdapterRepository extends ReactiveCrudRepository<AccountEntity, String>{

    Flux<AccountEntity> findByClientId(String clientId);

}
