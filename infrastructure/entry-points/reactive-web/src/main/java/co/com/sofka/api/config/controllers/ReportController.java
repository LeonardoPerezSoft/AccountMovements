package co.com.sofka.api.config.controllers;


import co.com.sofka.api.config.ReportDto;
import co.com.sofka.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/report")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/")
public class ReportController {

    private final ReportUseCase reportUseCase;

    @GetMapping("/{clientId}")
    public Mono<List<ReportDto>> findAccount(@RequestParam("initialDate") LocalDate initialDate,
                                             @RequestParam("finalDate") LocalDate finalDate,
                                             @PathVariable("clientId") String clientId) {

        return reportUseCase.generateReport(clientId, initialDate, finalDate)
                .map(transactions -> transactions.stream().map(ReportDto::mapToReportDto).collect(Collectors.toList()));
    }

}
