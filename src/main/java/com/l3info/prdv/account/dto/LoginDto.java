package com.l3info.prdv.account.dto;

import com.l3info.prdv.account.Account;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @Size(min = Account.LOGIN_LENGTH, max = Account.LOGIN_LENGTH, message = "L'identifiant doit être au format Multipass")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
