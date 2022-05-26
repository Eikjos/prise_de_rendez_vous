package com.l3info.prdv.account;

import com.l3info.prdv.account.exception.*;
import com.l3info.prdv.event.EventService;
import com.l3info.prdv.group.Group;
import com.l3info.prdv.group.GroupService;
import com.l3info.prdv.slot.SlotService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.l3info.prdv.slot.Slot;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EventService eventService;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    private final SlotService slotService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          EventService eventService,
                          GroupService groupService,
                          PasswordEncoder passwordEncoder,
                          SlotService slotService) {
        this.accountRepository = accountRepository;
        this.eventService = eventService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
        this.slotService = slotService;
    }

    // ---

    public void addToGroup(long accountId, long groupId) {
        Account account = findWithGroups(accountId);
        Group group = groupService.findOneWithMembers(groupId);
        if (account.getGroups().contains(group))
            throw new AccountAlreadyInGroupException();
        account.getGroups().add(group);
        group.getAccounts().add(account);
        accountRepository.save(account);
    }

    public Account authenticate(String username, String password) {
        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null || !passwordEncoder.matches(password, account.getPassword()))
            throw new InvalidCredentialsException();
        return account;
    }

    public void changePassword(long accountId, String oldPassword, String newPassword) {
        Account account = find(accountId);
        if (!passwordEncoder.matches(oldPassword, account.getPassword()))
            throw new InvalidCredentialsException();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public Account create(Account account) {
        if (!account.getType().isAssignable())
            throw new UnassignableTypeException();
        if (accountRepository.existsByUsername(account.getUsername()))
            throw new UsernameAlreadyUsedException();
        if (accountRepository.existsByEmail(account.getEmail()))
            throw new EmailAlreadyUsedException();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account created = accountRepository.save(account);
        return find(created.getId());
    }

    public Account find(Long id) {
        return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    public Account find(String username) {
        return accountRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public Account findWithGroups(Long id) {
        Account account = find(id);
        Hibernate.initialize(account.getGroups());
        return account;
    }

    @Transactional
    public Account findWithGroups(String username) {
        Account account = find(username);
        Hibernate.initialize(account.getGroups());
        return account;
    }

    public List<Account> findAll() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), true).toList();
    }

    public void update(long accountId, Account updatedAccount) {
        Account account = find(accountId);
        if (account.getType().isAssignable() && !updatedAccount.getType().isAssignable())
            throw new UnassignableTypeException();
        if (!account.getEmail().equals(updatedAccount.getEmail())
                && accountRepository.existsByEmail(updatedAccount.getEmail()))
            throw new EmailAlreadyUsedException();
        account.setFirstName(updatedAccount.getFirstName());
        account.setLastName(updatedAccount.getLastName());
        account.setEmail(updatedAccount.getEmail());
        account.setType(updatedAccount.getType());
        accountRepository.save(account);
    }

    @Transactional
    public void delete(Long id) {
        Account account = findWithGroups(id);
        account.getGroups().forEach(g -> g.getAccounts().remove(account));
        account.getBooked().forEach(s -> slotService.edit(s, null, null));
        account.getEvents().forEach(e -> eventService.delete(e.getId()));
        accountRepository.delete(account);
    }

    public void removeFromGroup(long accountId, long groupId) {
        Account account = findWithGroups(accountId);
        Group group = groupService.findOneWithMembers(groupId);
        if (!account.getGroups().contains(group))
            throw new AccountNotInGroupException();
        account.getGroups().remove(group);
        group.getAccounts().remove(account);
        accountRepository.save(account);
    }
}
