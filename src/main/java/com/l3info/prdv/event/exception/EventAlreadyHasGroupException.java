package com.l3info.prdv.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventAlreadyHasGroupException extends RuntimeException {
}
