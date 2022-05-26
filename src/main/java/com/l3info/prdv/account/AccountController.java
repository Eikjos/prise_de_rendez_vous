package com.l3info.prdv.account;

import com.l3info.prdv.account.dto.ChangePasswordDto;
import com.l3info.prdv.account.dto.CreateAccountDto;
import com.l3info.prdv.account.dto.UpdateAccountDto;
import com.l3info.prdv.account.exception.EmailAlreadyUsedException;
import com.l3info.prdv.account.exception.InvalidCredentialsException;
import com.l3info.prdv.account.exception.UnassignableTypeException;
import com.l3info.prdv.account.exception.UsernameAlreadyUsedException;
import com.l3info.prdv.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final GroupService groupService;

    @Autowired
    public AccountController(AccountService accountService, GroupService groupService) {
        this.accountService = accountService;
        this.groupService = groupService;
    }

    // --- MON COMPTE

    @GetMapping("/overview")
    public String overview(Authentication authentication, Model model) {
        Account account = accountService.findWithGroups(authentication.getName());
        model.addAttribute("account", account);
        return "account/overview";
    }

    // --- CHANGEMENT DE MOT DE PASSE

    @GetMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordDto") ChangePasswordDto dto) {
        return "account/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication,
                                 @Valid @ModelAttribute("changePasswordDto") ChangePasswordDto dto,
                                 BindingResult result) {
        if (result.hasErrors())
            return "account/change-password";
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("newPassword", "", "Les mots de passe ne correspondent pas");
            result.rejectValue("confirmPassword", "", "Les mots de passe ne correspondent pas");
            return "account/change-password";
        }
        try {
            Account account = (Account) authentication.getDetails();
            accountService.changePassword(account.getId(), dto.getAuthentication(), dto.getNewPassword());
        } catch (InvalidCredentialsException ex) {
            result.rejectValue("authentication", "", "Le mot de passe actuel n'est pas valide");
            return "account/change-password";
        }
        return "redirect:/account/overview?passwordChanged";
    }

    // --- CRÉATION DE COMPTE

    @GetMapping("/create")
    public String create(@ModelAttribute("createAccountDto") CreateAccountDto dto) {
        return "account/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("createAccountDto") CreateAccountDto dto,
                         BindingResult result) {
        if (result.hasErrors())
            return "account/create";
        try {
            Account account = accountService.create(dto.toAccount());
            return "redirect:/account/" + account.getId();
        } catch (UsernameAlreadyUsedException ex) {
            result.rejectValue("username", "", "L'identifiant est déjà utilisé par un autre compte");
        } catch (EmailAlreadyUsedException ex) {
            result.rejectValue("email", "", "L'adresse email est déjà utilisée par un autre compte");
        } catch (UnassignableTypeException ex) {
            result.rejectValue("type", "", "Ce type ne peut être assigné à l'inscription");
        }
        return "account/create";
    }

    // --- GESTION DE COMPTE

    @GetMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @ModelAttribute("updateAccountDto") UpdateAccountDto updateAccountDto,
                         Model model) {
        Account account = accountService.findWithGroups(id);
        model.addAttribute("account", account);
        model.addAttribute("groups", groupService.findAll(account.getGroups()));
        return "account/manage";
    }

    @PostMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @Valid @ModelAttribute("updateAccountDto") UpdateAccountDto updateAccountDto,
                         BindingResult result,
                         Model model) {
        Account account = accountService.findWithGroups(id);
        if (result.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("groups", groupService.findAll(account.getGroups()));
            return "account/manage";
        }
        try {
            accountService.update(id, updateAccountDto.toAccount());
            return "redirect:/account/" + id + "?updated";
        } catch (EmailAlreadyUsedException ex) {
            result.rejectValue("email", "", "L'adresse email est déjà utilisée par un autre compte");
        } catch (UnassignableTypeException ex) {
            result.rejectValue("type", "", "Ce type ne peut être assigné via l'interface");
        }
        model.addAttribute("account", account);
        model.addAttribute("groups", groupService.findAll(account.getGroups()));
        return "account/manage";
    }

    // --- AJOUT ET RETRAIT DE GROUPES

    @PostMapping("/{id}/group/{groupId}/add")
    public String addGroup(@PathVariable Long id, @PathVariable Long groupId) {
        accountService.addToGroup(id, groupId);
        return "redirect:/account/" + id;
    }

    @PostMapping("/{id}/group/{groupId}/remove")
    public String removeGroup(@PathVariable Long id, @PathVariable Long groupId) {
        accountService.removeFromGroup(id, groupId);
        return "redirect:/account/" + id;
    }

    // ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        accountService.delete(id);
        return "redirect:/account/list?deleted";
    }

    // --- LISTE DES COMPTES

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "account/list";
    }
}
