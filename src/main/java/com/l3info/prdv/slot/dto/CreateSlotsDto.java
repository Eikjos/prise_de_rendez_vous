package com.l3info.prdv.slot.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateSlotsDto {

    @NotNull(message = "Ce champ est obligatoire")
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "La date doit nécessairement représenter un moment futur")
    private LocalDateTime start;

    @NotNull(message = "Ce champ est obligatoire")
    @Range(min = 1L, max = 16L, message = "Le nombre de créneaux doit être compris entre 1 et 16")
    private Integer count;

    @NotNull(message = "Ce champ est obligatoire")
    @Range(min = 5L, max = 60L, message = "La durée d'un créneau doit être comprise entre 5 et 60 minutes")
    private Integer duration;
}
