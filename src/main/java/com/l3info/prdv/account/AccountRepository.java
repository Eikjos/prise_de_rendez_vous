package com.l3info.prdv.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

import com.l3info.prdv.slot.Slot;

public interface AccountRepository extends CrudRepository<Account, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Account> findByUsername(String username);

    @Query(
        value = "SELECT* FROM slot WHERE slot.event_id = ?1 AND slot.booker_id = ?2",
        nativeQuery = true
    )
    Set<Slot> findSlotByEventIdAndBookerId(Long eventId, Long accountId);
}
