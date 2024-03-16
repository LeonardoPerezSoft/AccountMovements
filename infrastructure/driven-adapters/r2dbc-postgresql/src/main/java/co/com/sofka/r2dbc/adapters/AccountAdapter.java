package co.com.sofka.r2dbc.adapters;

import co.com.sofka.model.Account;
import co.com.sofka.model.gateways.PersonRepository;
import co.com.sofka.model.gateways.RepositoryCrud;
import co.com.sofka.r2dbc.entities.AccountEntity;
import co.com.sofka.r2dbc.helper.CustomException;
import co.com.sofka.r2dbc.repositories.AccountAdapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
@RequiredArgsConstructor
public class AccountAdapter implements RepositoryCrud<Account, String> {

    private final AccountAdapterRepository accountRepository;
    private final PersonRepository personRepository;

    @Override
    public Mono<Account> findById(String id) {
        return accountRepository.findById(id)
                .flatMap(this::mapAccountEntityToAccount)
                .switchIfEmpty(Mono.error(new CustomException("El ID: "+ id + " NO tiene ninguna cuenta asociada")));
    }

    @Override
    public Flux<Account> findAll() {
        return accountRepository.findAll()
                .flatMap(this::mapAccountEntityToAccount);
    }

    @Override
    public Flux<Account> findAllById(String clientId) {
        return accountRepository.findByClientId(clientId)
                .flatMap(accountEntity -> mapAccountEntityToAccount(accountEntity));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return accountRepository.deleteById(id);

    }

    @Override
    public Mono<Account> update(Account account, String id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("La cuenta con ID " + id + " no existe")))
                .flatMap(existingAccount -> {

                    existingAccount.setNumberAccount(account.getNumberAccount());
                    existingAccount.setInitialBalance(account.getInitialBalance());
                    existingAccount.setState(account.isState());
                    existingAccount.setType(account.getType());

                    return personRepository.findClientName(account.getClientId())
                            .flatMap(clientName -> {
                                existingAccount.setClientName(clientName);
                                return accountRepository.save(existingAccount);
                            });
                })
                .flatMap(this::mapAccountEntityToAccount);
    }

    @Override
    public Mono<Account> create(Account account) {
        if (account == null || account.getClientId() == null || account.getNumberAccount() == null) {
            return Mono.error(new CustomException("Debe proporcionar todos los campos para crear una nueva cuenta"));
        }

        return personRepository.findClientName(account.getClientId())
                .flatMap(clientName -> {

                    AccountEntity accountEntity = AccountEntity.builder()
                            .numberAccount(account.getNumberAccount())
                            .initialBalance(account.getInitialBalance())
                            .state(account.isState())
                            .type(account.getType())
                            .clientName(clientName)
                            .clientId(account.getClientId())
                            .build();

                    return accountRepository.save(accountEntity);
                })
                .flatMap(this::mapAccountEntityToAccount);
    }


    private Mono<Account> mapAccountEntityToAccount(AccountEntity accountEntity) {
        return Mono.just(Account.builder()
                .accountId(accountEntity.getAccountId())
                .numberAccount(accountEntity.getNumberAccount())
                .initialBalance(accountEntity.getInitialBalance())
                .state(accountEntity.isState())
                .type(accountEntity.getType())
                .clientName(accountEntity.getClientName())
                .clientId(accountEntity.getClientId())
                .build());
    }


}