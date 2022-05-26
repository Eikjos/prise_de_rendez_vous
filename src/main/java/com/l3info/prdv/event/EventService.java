package com.l3info.prdv.event;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.dto.UpdateEventDto;
import com.l3info.prdv.event.exception.EventAlreadyExistsException;
import com.l3info.prdv.event.exception.EventAlreadyHasGroupException;
import com.l3info.prdv.event.exception.EventDoesntHaveGroupException;
import com.l3info.prdv.event.exception.EventNotFoundException;
import com.l3info.prdv.group.Group;
import com.l3info.prdv.group.GroupService;
import com.l3info.prdv.slot.SlotService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class EventService {

    private final GroupService groupService;
    private final EventRepository eventRepository;
    private final SlotService slotService;

    @Autowired
    public EventService(GroupService groupService, EventRepository eventRepository, SlotService slotService) {
        this.groupService = groupService;
        this.eventRepository = eventRepository;
        this.slotService = slotService;
    }

    // ---

    @Transactional
    public void addGroup(long eventId, long groupId) {
        Event event = findWithGroups(eventId);
        Group group = groupService.findOne(groupId);
        if (event.getGroups().contains(group))
            throw new EventAlreadyHasGroupException();
        event.getGroups().add(group);
        group.getEvents().add(event);
        eventRepository.save(event);
    }

    public Event create(Event event) {
        if (eventRepository.existsByName(event.getName()))
            throw new EventAlreadyExistsException();
        return eventRepository.save(event);
    }

    public Event find(Long id) {
        return eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
    }

    @Transactional
    public Event findWithGroups(Long id) {
        Event event = find(id);
        Hibernate.initialize(event.getGroups());
        return event;
    }

    public Set<Event> findAll() {
        return eventRepository.findAll();
    }

    public Set<Event> findAllByUserId(Long userId) {
        return eventRepository.findAllByUserId(userId);
    }

    public Set<Event> findAllByAuthor(Account account) {
        return eventRepository.findByAuthor(account);
    }

    public void update(Long eventId, UpdateEventDto dto) {
        if (eventRepository.existsByIdNotAndName(eventId, dto.getName()))
            throw new EventAlreadyExistsException();
        Event event = find(eventId);
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        eventRepository.save(event);
    }


    @Transactional
    public void delete(Long id) {
        Event event = findWithGroups(id);
        event.getGroups().forEach(g -> g.getEvents().remove(event));
        event.getGroups().clear();
        eventRepository.delete(event);
    }

    @Transactional
    public void removeGroup(long eventId, long groupId) {
        Event event = findWithGroups(eventId);
        Group group = groupService.findOne(groupId);
        if (!event.getGroups().contains(group))
            throw new EventDoesntHaveGroupException();
        event.getGroups().remove(group);
        group.getEvents().remove(event);
        eventRepository.save(event);
    }
}
