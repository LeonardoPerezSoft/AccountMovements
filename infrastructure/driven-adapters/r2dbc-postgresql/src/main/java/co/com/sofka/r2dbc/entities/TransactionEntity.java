package co.com.sofka.r2dbc.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @Column("transaction_number")
    private int transactionNumber;
    private LocalDate date;
    private String movementType;
    private float amount;
    private float balance;
    @Column("account_id")
    private int accountId;
}
