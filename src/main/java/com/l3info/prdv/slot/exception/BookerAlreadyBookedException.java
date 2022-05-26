package com.l3info.prdv.slot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookerAlreadyBookedException extends RuntimeException {
}
