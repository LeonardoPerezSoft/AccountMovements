package co.com.sofka.r2dbc.adapters;



import co.com.sofka.model.Account;
import co.com.sofka.model.Transaction;
import co.com.sofka.model.gateways.RepositoryCrud;
import co.com.sofka.model.gateways.TransactionRepository;
import co.com.sofka.r2dbc.entities.AccountEntity;
import co.com.sofka.r2dbc.entities.TransactionEntity;
import co.com.sofka.r2dbc.helper.CustomException;
import co.com.sofka.r2dbc.repositories.TransactionAdapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionAdapter implements RepositoryCrud<Transaction, String>, TransactionRepository {

    private final TransactionAdapterRepository transactionAdapterRepository;

    @Override
    public Mono<Transaction> findById(String id) {
        return transactionAdapterRepository.findById(id)
                .flatMap(transactionEntity -> Mono.just(mapToTransaction(transactionEntity)))
                .switchIfEmpty(Mono.error(new CustomException("El numero de transaccion: "+ id + " NO tiene ningún registro")));
    }

    @Override
    public Flux<Transaction> findAll(){
        return transactionAdapterRepository.findAll()
                .flatMap(transactionEntity -> Mono.just(mapToTransaction(transactionEntity)));
    }

    @Override
    public Mono<Transaction> create(Transaction transaction) {
        int accountId = transaction.getAccountId();

          return transactionAdapterRepository.findTransactionsNumber(accountId)
                .flatMap(transactionsNumber -> {
                    if (transactionsNumber > 0) {
                      return updateBalanceWithTransaction(accountId, transaction);
                    } else {
                     return updateBalanceWithInitialBalance(accountId, transaction);
                    }
                });
    }

    private Mono<Transaction> updateBalanceWithTransaction(int accountId, Transaction transaction) {
           return transactionAdapterRepository.findCurrentBalance(accountId)
                .flatMap(currentBalance -> {
                   float newBalance = currentBalance + transaction.getAmount();
                    if (newBalance < 0) {
                        return Mono.error(new CustomException("Saldo no disponible"));
                    }
                    TransactionEntity transactionEntity = mapToTransactionEntity(transaction);
                    transactionEntity.setBalance( newBalance);

                    return transactionAdapterRepository.save(transactionEntity)
                            .thenReturn(mapToTransaction(transactionEntity));
                });
    }

    private Mono<Transaction> updateBalanceWithInitialBalance(int accountId, Transaction transaction) {
        return transactionAdapterRepository.findInitialBalance(accountId)
                .flatMap(initialBalance -> {
                       double newBalance = initialBalance + transaction.getAmount();

                    if (newBalance < 0) {
                        return Mono.error(new CustomException("Saldo no disponible"));
                    }

                    TransactionEntity transactionEntity = mapToTransactionEntity(transaction);
                    transactionEntity.setBalance((float) newBalance);

                    return transactionAdapterRepository.save(transactionEntity)
                            .thenReturn(mapToTransaction(transactionEntity));
                });
    }

    @Override
    public Flux<Transaction> findAllById(String s) {
        return null;
    }


    @Override
    public Flux<Transaction> findAllTransactionByAccountId(int accountId, LocalDate initialDate, LocalDate finalDate) {
        return transactionAdapterRepository.findAllTransactionByAccountId(initialDate, finalDate, accountId)
                .flatMap(transactionEntity -> Mono.just(mapToTransaction(transactionEntity)));
    }

    @Override
    public Mono<Float> findCurrentBalanceByAccountId(int accountId) {
        return transactionAdapterRepository.findInitialBalance(accountId)
                .switchIfEmpty(Mono.just((float) 0.0));
    }
    @Override
    public Mono<Float> findCurrentBalance(int accountId) {
        return transactionAdapterRepository.findCurrentBalance(accountId)
                .switchIfEmpty(Mono.just((float) 0.0));
    }

    @Override
    public Mono<Integer> findTransactionsNumber(int accountId) {
        return transactionAdapterRepository.findTransactionsNumber(accountId)
                .switchIfEmpty(Mono.just(0));
    }


    @Override
    public Mono<Void> deleteById(String id) {
        return transactionAdapterRepository.deleteById(id);

    }

    @Override
    public Mono<Transaction> update(Transaction T, String s) {
        return null;
    }


    private TransactionEntity mapToTransactionEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .transactionNumber(transaction.getTransactionNumber())
                .date(transaction.getDate())
                .movementType(transaction.getMovementType())
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .accountId(transaction.getAccountId())
                .build();
    }

    private Transaction mapToTransaction(TransactionEntity transactionEntity) {
        return Transaction.builder()
                .transactionNumber(transactionEntity.getTransactionNumber())
                .date(transactionEntity.getDate())
                .movementType(transactionEntity.getMovementType())
                .amount(transactionEntity.getAmount())
                .balance(transactionEntity.getBalance())
                .accountId(transactionEntity.getAccountId())
                .build();
    }




}
