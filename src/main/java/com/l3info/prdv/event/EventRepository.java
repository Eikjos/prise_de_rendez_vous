package com.l3info.prdv.event;

import com.l3info.prdv.account.Account;
import org.springframework.data.jpa.repository.Query;

import com.l3info.prdv.group.Group;
import com.l3info.prdv.slot.Slot;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface EventRepository extends CrudRepository<Event, Long> {

    boolean existsByName(String name);

    boolean existsByIdNotAndName(Long id, String name);

    Optional<Event> findByName(String name);

    Set<Event> findAll();

    @Query(
            value = "SELECT * FROM event WHERE event.id IN ("
                + "SELECT events_id FROM group_events WHERE group_events.groups_id IN ("
                    + "SELECT groups_id FROM group_accounts WHERE group_accounts.accounts_id = ?1))",
            nativeQuery = true)
    Set<Event> findAllByUserId(Long userId);

    Set<Event> findByAuthor(Account author);

    Set<Event> findAllByGroupsContaining(Group group);
}
