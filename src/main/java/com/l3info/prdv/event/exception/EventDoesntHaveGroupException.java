package com.l3info.prdv.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventDoesntHaveGroupException extends RuntimeException {
}
