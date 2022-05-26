package com.l3info.prdv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountType;
import com.l3info.prdv.event.EventService;
import com.l3info.prdv.slot.SlotService;

@Controller
public class RootController {

    @Autowired
    private EventService eventService;

    @Autowired
    private SlotService slotService;

    @GetMapping("/")
    public String home(Model model, Authentication authenticate) {
        Account account = (Account) authenticate.getDetails();
        if (account.getType() == AccountType.STUDENT) {
            model.addAttribute("events", eventService.findAllByUserId(account.getId()));
            model.addAttribute("slots", slotService.findAll(account));
            return "studentHome";
        } else {
            model.addAttribute("events", eventService.findAllByAuthor(account));
            return "teacherHome";
        }
    }

    @GetMapping("/books")
    public String slotsList(Model model, Authentication authenticate) {
        Account account = (Account) authenticate.getDetails();
        model.addAttribute("slots", slotService.findAll(account));
        return "slot/list";
    }
}

