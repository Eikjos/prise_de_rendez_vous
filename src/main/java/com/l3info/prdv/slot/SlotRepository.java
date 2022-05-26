package com.l3info.prdv.slot;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.Event;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface SlotRepository extends CrudRepository<Slot, Long> {

    boolean existsByEventAndBooker(Event event, Account booker);

    Slot findByEventAndBooker(Event event, Account booker);
    boolean existsByEventAndStart(Event event, LocalDateTime start);

    Set<Slot> findAllByBooker(Account booker);

    Set<Slot> findAllByEvent(Event event);
}
