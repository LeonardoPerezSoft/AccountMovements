package co.com.sofka.api.config.controllers;

import co.com.sofka.model.Account;
import co.com.sofka.r2dbc.helper.CustomException;
import co.com.sofka.usecase.account.AccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class AccountController {

    private final AccountUseCase accountUseCase;

    @GetMapping
    public Flux<Account> findAllAcounts() {
        return accountUseCase.findAllAccounts()
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));

    }

    @GetMapping("/{id}")
    public Mono<Account> findByIdAccount(@PathVariable("id") String id) {
        return accountUseCase.findAccountById(id)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }



    @PostMapping
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountUseCase.createAccount(account)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));

    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteAccount(@PathVariable("id") String id) {
        return accountUseCase.findAccountById(id)
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(product -> accountUseCase.deleteAccountById(id));
    }


    @PutMapping("/{id}")
    public Mono<Account> updateAccount(@RequestBody Account account, @PathVariable("id") String id) {
        return accountUseCase.updateAccount(account, id)
                  .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }



}
