package com.l3info.prdv.slot;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountService;
import com.l3info.prdv.event.EventService;
import com.l3info.prdv.slot.dto.BookSlotDto;
import com.l3info.prdv.slot.dto.CreateSlotsDto;
import com.l3info.prdv.slot.dto.EditSlotDto;
import com.l3info.prdv.slot.exception.SlotAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/event/{eventId}/slot")
public class SlotController {

    private final AccountService accountService;
    private final EventService eventService;
    private final SlotService slotService;

    @Autowired
    public SlotController(AccountService accountService, EventService eventService, SlotService slotService) {
        this.accountService = accountService;
        this.eventService = eventService;
        this.slotService = slotService;
    }

    @GetMapping("/create")
    public String createBatch(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventService.find(eventId));
        model.addAttribute("createSlotsDto", new CreateSlotsDto());
        return "slot/create";
    }

    @PostMapping("/create")
    public String createBatch(@PathVariable Long eventId,
                              Model model,
                              @Valid @ModelAttribute CreateSlotsDto createSlotsDto,
                              BindingResult result) {
        model.addAttribute("event", eventService.find(eventId));
        if (result.hasErrors())
            return "slot/create";
        try {
            slotService.createBatch(createSlotsDto, eventService.find(eventId));
        } catch (SlotAlreadyExistsException ex) {
            String msg = "Un ou plusieurs créneaux existent dans la plage souhaitée.";
            result.rejectValue("start", "", msg);
            result.rejectValue("count", "", msg);
            result.rejectValue("duration", "", msg);
            return "slot/create";
        }
        return "redirect:/event/{eventId}";
    }

    // ---
    @GetMapping("/{id}")
    public String resume(@PathVariable Long id, Model model) {
        model.addAttribute("slot", slotService.findOne(id));
        return "slot/modify-book";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Slot slot =  slotService.findOne(id);
        model.addAttribute("slot", slot);
        EditSlotDto dto = new EditSlotDto();
        dto.setBookerId(slot.getBooker() == null ? -1 : slot.getBooker().getId());
        dto.setComment(slot.getComment());
        model.addAttribute("editBookerDto", dto);
        return "slot/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @ModelAttribute EditSlotDto editSlotDto) {
        Slot slot = slotService.findOne(id);
        Account account = editSlotDto.getBookerId() == -1 ? null : accountService.find(editSlotDto.getBookerId());
        slotService.edit(slot, account, editSlotDto.getComment().isBlank() ? null : editSlotDto.getComment());
        return "redirect:/event/{eventId}/slot/{id}/edit?updated";
    }

    @GetMapping("/{id}/book")
    public String book(@PathVariable Long id, Model model) {
        Slot slot = slotService.findOne(id);
        model.addAttribute("bookSlotDto", new BookSlotDto());
        model.addAttribute("slot", slot);
        return "slot/book";
    }

    @PostMapping("/{id}/book")
    public String book(@PathVariable Long id, Authentication authentication, @Valid @ModelAttribute BookSlotDto bookSlotDto, BindingResult result) {
        Slot slot = slotService.findOne(id);
        Account account = (Account) authentication.getDetails();
        slotService.book(
                slot,
                account,
                (bookSlotDto.getComment() == null || bookSlotDto.getComment().isBlank())
                        ? null
                        : bookSlotDto.getComment());
        return "redirect:/?booked";
    }

    // ---

    @PostMapping("/{id}/unbook")
    public String unbook(@PathVariable Long eventId, @PathVariable Long id, Authentication authentication) {
        Slot slot = slotService.findOne(id);
        Account account = (Account) authentication.getDetails();
        if (!account.equals(slot.getBooker()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        slotService.edit(slot, null, null);
        return "redirect:/event/{eventId}/slots?cancelled";
    }

    // ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Slot slot = slotService.findOne(id);
        slotService.delete(slot);
        return "redirect:/event/" + slot.getEvent().getId() + "?slotDeleted";
    }
}
