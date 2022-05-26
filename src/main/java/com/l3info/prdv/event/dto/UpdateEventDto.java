package com.l3info.prdv.event.dto;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.Event;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateEventDto {

    @NotBlank(message = "Le nom de l'événement est obligatoire")
    @Size(
            max = Event.NAME_LENGTH,
            message = "Le nom de l'événement ne doit excéder " + Event.NAME_LENGTH + " caractères")
    private String name;

    @Size(
            max = Event.DESC_LENGTH,
            message = "La description de l'événement ne doit excéder " + Event.DESC_LENGTH + " caractères")
    private String description;

    @Size(
            max = Event.LOCA_LENGTH,
            message = "L'emplacement de l'événement ne doit excéder " + Event.LOCA_LENGTH + " caractères")
    private String location;

    // ---

    public Event toEvent(Account author) {
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setLocation(location);
        event.setAuthor(author);
        return event;
    }
}
