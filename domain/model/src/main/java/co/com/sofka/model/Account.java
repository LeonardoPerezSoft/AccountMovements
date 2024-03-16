package co.com.sofka.model;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {


    private int accountId;
    private String numberAccount;
    private float initialBalance;
    private boolean state;
    private String type;
    private String clientName;
    private String clientId;


 }
