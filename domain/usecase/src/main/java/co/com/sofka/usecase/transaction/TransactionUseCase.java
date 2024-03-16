package co.com.sofka.usecase.transaction;

import co.com.sofka.model.Transaction;
import co.com.sofka.model.gateways.RepositoryCrud;
import co.com.sofka.model.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RequiredArgsConstructor
public class TransactionUseCase {

    private final RepositoryCrud<Transaction, String> repositoryCrud;
    private final TransactionRepository transactionRepository;


    public Flux<Transaction> findAllTransactions(){
        return repositoryCrud.findAll();
    }

    public Mono<Transaction> findTransactionById(String id){
        return repositoryCrud.findById(id);

    }

    public Mono<Integer> findTransactionsNumber (int accountId){
        return transactionRepository.findTransactionsNumber(accountId);
    }


    public Mono<Transaction> createTransaction(Transaction transaction) {
        int accountId = transaction.getAccountId();


        return transactionRepository.findCurrentBalanceByAccountId(accountId)
                .flatMap(initialBalance -> {
                    double newBalance = initialBalance + transaction.getAmount();
                    transaction.setBalance((float) newBalance);
                    return repositoryCrud.create(transaction);
                });
    }


    public Mono<Transaction> updateTransaction(Transaction transaction, String  id){
        return repositoryCrud.update(transaction, id);
    }


    public Mono<Void> deleteTransactionById(String id) {
        return repositoryCrud.deleteById(id);
    }



    public Mono<Float> findCurrentBalance(int accountId) {
    return transactionRepository.findCurrentBalanceByAccountId(accountId);
    }

    public Mono<Float> findCurrentBalanceTransaction(int accountId) {
        return transactionRepository.findCurrentBalance(accountId);

    }


}
