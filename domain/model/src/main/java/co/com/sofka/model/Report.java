package co.com.sofka.model;
import lombok.*;

import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {
    private LocalDate date;
    private String clientName;
    private String numberAccount;
    private String type;
    private float initialBalance;
    private Boolean state;
    private float transaction;
    private float balance;


}

