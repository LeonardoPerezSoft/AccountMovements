package co.com.sofka.model.gateways;

import co.com.sofka.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TransactionRepository {

    Flux<Transaction> findAllTransactionByAccountId(int accountId, LocalDate initialDate, LocalDate finalDate);

    Mono<Float> findCurrentBalanceByAccountId(int accountId);

    Mono<Float>findCurrentBalance(int accountId);

   Mono<Integer> findTransactionsNumber(int accountId);
}
