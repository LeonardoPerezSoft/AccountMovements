package co.com.sofka.api.config;

import co.com.sofka.model.Report;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record ReportDto(LocalDate fecha, String nombreCliente, String numeroCuenta, String tipo, Float saldoInicial, Boolean estado, Float movimiento,
                        Float saldoDisponible) {

    public static ReportDto mapToReportDto (Report report) {
        return ReportDto.builder()
                .fecha(report.getDate())
                .nombreCliente(report.getClientName())
                .numeroCuenta(report.getNumberAccount())
                .tipo(report.getType())
                .saldoInicial(report.getInitialBalance())
                .estado(report.getState())
                .movimiento(report.getTransaction())
                .saldoDisponible(report.getBalance())
                .build();

    }
}

