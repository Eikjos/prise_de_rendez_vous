package com.l3info.prdv.group.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GroupAlreadyExistsException extends RuntimeException {
}
