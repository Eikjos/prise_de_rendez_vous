package com.l3info.prdv.account.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "Le mot de passe actuel est obligatoire")
    private String authentication;

    @NotBlank(message = "Un nouveau mot de passe est obligatoire")
    private String newPassword;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmPassword;
}
