package co.com.sofka.usecase.report;

import co.com.sofka.model.Account;
import co.com.sofka.model.Report;
import co.com.sofka.model.gateways.RepositoryCrud;
import co.com.sofka.model.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ReportUseCase {

    private final RepositoryCrud<Account, String> repositoryCrud;
    private final TransactionRepository transactionRepository;


    public Mono<List<Report>> generateReport(String clientId, LocalDate initialDate, LocalDate finalDate) {
        return repositoryCrud.findAllById(clientId)
                .flatMap(account -> {
                    int accountId = account.getAccountId();
                    return findTransactions(account, initialDate, finalDate);
                })
                .collectList();
    }

    private Flux<Report> findTransactions(Account account, LocalDate initialDate, LocalDate finalDate) {
        return transactionRepository.findAllTransactionByAccountId(account.getAccountId(), initialDate, finalDate)
                .map(transaction -> Report.builder()
                        .date(transaction.getDate())
                        .clientName(account.getClientName())
                        .numberAccount(account.getNumberAccount())
                        .type(account.getType())
                        .initialBalance(account.getInitialBalance())
                        .state(account.isState())
                        .transaction(transaction.getAmount())
                        .balance(transaction.getBalance())
                        .build());
    }


}
