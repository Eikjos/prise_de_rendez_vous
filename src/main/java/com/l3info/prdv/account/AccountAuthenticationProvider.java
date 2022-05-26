package com.l3info.prdv.account;

import com.l3info.prdv.account.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;

@Component
@Controller
public class AccountAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    @Autowired
    public AccountAuthenticationProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    // ---

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountService.authenticate(username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username,
                password,
                Set.of(account.getType()));
        token.setDetails(account);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // ---

    @GetMapping("/login")
    public String login() {
        return "signin";
    }
}
