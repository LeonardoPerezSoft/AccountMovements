package co.com.sofka.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity {

    @Id
    @Column("account_id")
    private int accountId;
    private String numberAccount;
    private float initialBalance;
    private boolean state;
    private String type;
    private String clientName;
    @Column("client_id")
    private String clientId;





}
