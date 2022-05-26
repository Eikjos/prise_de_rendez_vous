package com.l3info.prdv.configuration;

import com.l3info.prdv.account.AccountAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final AccountAuthenticationProvider accountAuthenticationProvider;

    @Autowired
    public WebSecurityConfigurer(AccountAuthenticationProvider accountAuthenticationProvider) {
        this.accountAuthenticationProvider = accountAuthenticationProvider;
    }

    // ---

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(accountAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                    .antMatchers("/css/*", "/fonts/*", "/img/*", "/js/*", "/favicon.png").permitAll()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .and()
                .logout();
    }
}
