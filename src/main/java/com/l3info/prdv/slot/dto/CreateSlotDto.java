package com.l3info.prdv.slot.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateSlotDto {

    @NotNull(message = "Ce champ est obligatoire")
    private LocalDateTime start;

    // ---

    public static CreateSlotDto of(LocalDateTime start) {
        CreateSlotDto dto = new CreateSlotDto();
        dto.setStart(start);
        return dto;
    }
}
