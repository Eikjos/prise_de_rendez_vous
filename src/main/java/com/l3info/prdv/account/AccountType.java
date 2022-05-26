package com.l3info.prdv.account;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum AccountType implements GrantedAuthority {
    STUDENT("Étudiant", true),
    TEACHER("Enseignant", true),
    ADMIN("Administrateur", false);

    // ---

    private final String description;
    private final boolean assignable;

    public boolean isAssignable() {
        return assignable;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    @Override
    public String toString() {
        return description;
    }

    public static AccountType accountTypeFromString (String s) {
        return switch (s) {
            case "student" -> AccountType.STUDENT;
            case "teacher" -> AccountType.TEACHER;
            case "admin" -> AccountType.ADMIN;
            default -> null;
        };
    }
}
