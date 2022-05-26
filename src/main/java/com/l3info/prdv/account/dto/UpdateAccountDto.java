package com.l3info.prdv.account.dto;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateAccountDto {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = Account.NAMES_LENGTH, message = "Le prénom ne doit excéder " + Account.NAMES_LENGTH + " caractères")
    private String firstName;

    @NotBlank(message = "Le nom de famille est obligatoire")
    @Size(max = Account.NAMES_LENGTH, message = "Le nom de famille ne doit excéder " + Account.NAMES_LENGTH + " caractères")
    private String lastName;

    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "L'adresse email doit être valide")
    private String email;

    @NotNull(message = "Le type de compte est obligatoire")
    private AccountType type;

    // ---

    public Account toAccount() {
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setType(type);
        return account;
    }
}
