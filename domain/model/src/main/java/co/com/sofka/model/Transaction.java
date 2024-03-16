package co.com.sofka.model;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {

    private int transactionNumber;
    private LocalDate date;
    private String movementType;
    private float amount;
    private float balance;
    private int accountId;

}

