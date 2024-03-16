package co.com.sofka.api.config.controllers;


import co.com.sofka.model.Account;
import co.com.sofka.model.Transaction;
import co.com.sofka.r2dbc.helper.CustomException;
import co.com.sofka.usecase.transaction.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    @GetMapping
    public Flux<Transaction> findAllTransactions() {
        return transactionUseCase.findAllTransactions()
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));

    }

    @GetMapping("/{id}")
    public Mono<Transaction> findByIdTransaction(@PathVariable("id") String id) {
        return transactionUseCase.findTransactionById(id)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    public Mono<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return transactionUseCase.createTransaction(transaction)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));

    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTransaction(@PathVariable("id") String id) {
        return transactionUseCase.findTransactionById(id)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(product -> transactionUseCase.deleteTransactionById(id));
    }


    @GetMapping("/Number/{accountId}")
    public Mono<Integer> findTransacionNumber(@PathVariable("accountId") int accountId){
        return transactionUseCase.findTransactionsNumber(accountId)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/balance/{accountId}")
    public Mono<Float> findCurrentBalanceByAccountId(@PathVariable("accountId") int accountId){
        return transactionUseCase.findCurrentBalance(accountId)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/balanceT/{accountId}")
    public Mono<Float> findCurrentBalanceTransaction(@PathVariable("accountId") int accountId){
        return transactionUseCase.findCurrentBalanceTransaction(accountId)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }



    @PutMapping("/{id}")
    public Mono<Transaction> updateTransaction(@RequestBody Transaction transaction, @PathVariable("id") String id) {
        return transactionUseCase.updateTransaction(transaction, id)
                 .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

}
