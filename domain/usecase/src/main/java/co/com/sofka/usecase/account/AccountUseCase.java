package co.com.sofka.usecase.account;

import co.com.sofka.model.Account;
import co.com.sofka.model.gateways.PersonRepository;
import co.com.sofka.model.gateways.RepositoryCrud;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AccountUseCase {

    private final RepositoryCrud<Account, String> repositoryCrud;
    private final PersonRepository personRepository;


    public Flux<Account> findAllAccounts(){
        return repositoryCrud.findAll();
    }

    public Mono<Account> findAccountById(String id){
        return repositoryCrud.findById(id);

    }

    public Mono<Account> createAccount(Account account){
        return personRepository.findClientName(account.getClientId())
                .flatMap(clientName -> repositoryCrud.create(account.toBuilder().clientName(clientName).build()));
    }

    public Mono<Account> updateAccount(Account account, String  id){
        return repositoryCrud.update(account, id);
    }


    public Mono<Void> deleteAccountById(String id) {
        return repositoryCrud.deleteById(id);
    }




}
