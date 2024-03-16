package co.com.sofka.r2dbc.repositories;

import co.com.sofka.r2dbc.entities.TransactionEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TransactionAdapterRepository extends ReactiveCrudRepository<TransactionEntity, String>{


    @Query("SELECT balance FROM transaction WHERE transaction_number = (SELECT MAX(transaction_number) FROM transaction WHERE account_id = :accountId)")
    Mono<Float> findCurrentBalance(@Param("accountId")int accountId);

    @Query(value = "select count(*) from transaction WHERE account_id = :accountId")
    Mono<Integer> findTransactionsNumber(@Param("accountId") int accountId);

    @Query(value = "SELECT * FROM transaction WHERE ((date  >= :initialDate )  AND  (date <= :finalDate) AND account_id = :accountId)")
    Flux<TransactionEntity> findAllTransactionByAccountId( @Param("initialDate")LocalDate initialDate,
                                                           @Param("finalDate") LocalDate finalDate,
                                                           @Param("accountId") int accountId);

    @Modifying
    @Query("UPDATE account SET initial_balance = :newBalance WHERE account_id = :accountId")
    Mono<Void> updateBalance(int accountId, float newBalance);

    @Modifying
    @Query("UPDATE account SET transaction_number = :transactionsNumber WHERE account_id = :accountId")
    Mono<Void> updateTransactionsNumber(int accountId, int transactionsNumber);

    @Query("SELECT initial_balance FROM account WHERE account_id = :accountId")
    Mono<Float> findInitialBalance(int accountId);



}
