package com.l3info.prdv.event;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountService;
import com.l3info.prdv.account.AccountType;
import com.l3info.prdv.event.dto.CreateEventDto;
import com.l3info.prdv.event.dto.UpdateEventDto;
import com.l3info.prdv.event.exception.EventAlreadyExistsException;
import com.l3info.prdv.group.GroupService;
import com.l3info.prdv.slot.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/event")
public class EventController {

    private final AccountService accountService;
    private final EventService eventService;
    private final GroupService groupService;
    private final SlotService slotService;

    @Autowired
    public EventController(EventService eventService, GroupService groupService, SlotService slotService, AccountService accountService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.slotService = slotService;
        this.accountService = accountService;
    }

    // ---

    @GetMapping("/list")
    public String list(Model model, Authentication authenticate) {
        Account account = (Account) authenticate.getDetails();
        if (account.getType() == AccountType.STUDENT) {
            model.addAttribute("events", eventService.findAllByUserId(account.getId()));
            return "studentCourses";
        } else if (account.getType() == AccountType.TEACHER) {
            model.addAttribute("events", eventService.findAllByAuthor(account));
            return "teacherCourses";
        } else {
            model.addAttribute("events", eventService.findAll());
            return "event/list";
        }
    }

    // ---

    @GetMapping("/create")
    public String create(@ModelAttribute("createEventDto") CreateEventDto createEventDto) {
        return "event/create";
    }

    @PostMapping("/create")
    public String create(Authentication authentication,
                         @Valid @ModelAttribute("createEventDto") CreateEventDto createEventDto,
                         BindingResult result) {
        if (result.hasErrors())
            return "event/create";
        try {
            Event event = eventService.create(createEventDto.toEvent((Account) authentication.getDetails()));
            return "redirect:/event/" + event.getId();
        } catch (EventAlreadyExistsException ex) {
            result.rejectValue("name", "", "Ce nom est déjà utilisé par un autre événement");
            return "event/create";
        }
    }

    // ---

    @GetMapping("/{id}/print")
    public String print(@PathVariable Long id, Model model) {
        Event event = eventService.find(id);
        model.addAttribute("event", event);
        model.addAttribute("groups", groupService.findAll(event.getGroups()));
        model.addAttribute("days", slotService.findAllPartitionedByDay(event));
        return "event/print";
    }

    @GetMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @ModelAttribute("updateEventDto") UpdateEventDto updateEventDto,
                         Model model) {
        Event event = eventService.findWithGroups(id);
        model.addAttribute("event", event);
        model.addAttribute("groups", groupService.findAll(event.getGroups()));
        model.addAttribute("slots", slotService.findAllPartitionedByDay(event));
        return "event/manage";
    }

    @PostMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @Valid @ModelAttribute("updateEventDto") UpdateEventDto dto,
                         BindingResult result,
                         Model model) {
        Event event = eventService.findWithGroups(id);
        if (result.hasErrors()) {
            model.addAttribute("event", event);
            model.addAttribute("groups", groupService.findAll(event.getGroups()));
            model.addAttribute("slots", slotService.findAll(event));
            return "event/manage";
        }
        try {
            eventService.update(id, dto);
            return "redirect:/event/{id}?updated";
        } catch (EventAlreadyExistsException ex) {
            result.rejectValue("name", "", "Ce nom est déjà utilisé par un autre événement");
            model.addAttribute("event", event);
            model.addAttribute("groups", groupService.findAll(event.getGroups()));
            model.addAttribute("slots", slotService.findAll(event));
            return "event/manage";
        }
    }

    @GetMapping("/{id}/slots")
    public String Slots(@PathVariable Long id, Authentication authenticate, Model model) {
        Account account = (Account) authenticate.getDetails();
        Event event = eventService.find(id);
        model.addAttribute("r", slotService.findByEventAndBooker(account, event));
        model.addAttribute("slots", slotService.findAllPartitionedByDay(event));
        return "slot/take-book";
    }

    // ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.delete(id);
        return "redirect:/event/list?deleted";
    }

    // ---

    @PostMapping("/{eventId}/group/{groupId}/add")
    public String addGroup(@PathVariable Long eventId, @PathVariable Long groupId) {
        eventService.addGroup(eventId, groupId);
        return "redirect:/event/{eventId}?added";
    }

    @PostMapping("/{eventId}/group/{groupId}/remove")
    public String removeGroup(@PathVariable Long eventId, @PathVariable Long groupId) {
        eventService.removeGroup(eventId, groupId);
        return "redirect:/event/{eventId}?removed";
    }
}
