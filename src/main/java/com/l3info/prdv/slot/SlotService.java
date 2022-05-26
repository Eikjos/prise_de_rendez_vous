package com.l3info.prdv.slot;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.Event;
import com.l3info.prdv.slot.dto.CreateSlotDto;
import com.l3info.prdv.slot.dto.CreateSlotsDto;
import com.l3info.prdv.slot.exception.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    //- GESTION DE CRÉNEAUX

    public void create(CreateSlotDto dto, Event event) {
        if (slotRepository.existsByEventAndStart(event, dto.getStart()))
            throw new SlotAlreadyExistsException();
        Slot slot = new Slot();
        slot.setStart(dto.getStart());
        slot.setEvent(event);
        slot.setBooker(null);
        slotRepository.save(slot);
    }

    @Transactional
    public void createBatch(CreateSlotsDto dto, Event event) {
        for (int i = 0; i < dto.getCount(); ++i)
            create(CreateSlotDto.of(dto.getStart().plusMinutes(dto.getDuration() * i)), event);
    }

    public void edit(Slot slot, Account booker, String comment) {
        slot.setBooker(booker);
        slot.setComment(comment);
        slotRepository.save(slot);
    }

    public void delete(Slot slot) {
        if (!slotRepository.existsById(slot.getId()))
            throw new SlotNotFoundException();
        slotRepository.delete(slot);
    }

    //- RECHERCHE DE CRÉNEAUX

    public Set<Slot> findAll(Account account) {
        return slotRepository.findAllByBooker(account);
    }

    public Set<Slot> findAll(Event event) {
        return slotRepository.findAllByEvent(event);
    }

    public Slot findOne(Long id) {
        return slotRepository.findById(id).orElseThrow(SlotNotFoundException::new);
    }

    public boolean isRegistered(Account account, Event event) {
        return slotRepository.existsByEventAndBooker(event, account);
    }

    public Slot findByEventAndBooker(Account account, Event event) {
        return slotRepository.findByEventAndBooker(event, account);
    }

    public Map<LocalDate, List<Slot>> findAllPartitionedByDay(Event event) {
        return slotRepository.findAllByEvent(event)
                .parallelStream().collect(Collectors.groupingBy(s -> s.getStart().toLocalDate()));
    }

    public Map<LocalDate, Map<Integer, List<Slot>>> findAllPartitionedByDayAndHour(Event event) {
        Map<LocalDate, List<Slot>> days = findAllPartitionedByDay(event);
        Map<LocalDate, Map<Integer, List<Slot>>> map = new HashMap<>();
        for (Map.Entry<LocalDate, List<Slot>> entry : days.entrySet()) {
            Map<Integer, List<Slot>> byHour = entry.getValue().parallelStream().collect(Collectors.groupingBy(s -> s.getStart().getHour()));
            map.put(entry.getKey(), byHour);
        }
        return map;
    }

    //- RÉSERVATIONS DE CRÉNEAUX

    public void book(Slot slot, Account booker, String comment) {
        if (slotRepository.existsByEventAndBooker(slot.getEvent(), booker))
            throw new BookerAlreadyBookedException();
        if (slot.getBooker() != null)
            throw new SlotAlreadyBookedException();
        slot.setBooker(booker);
        slot.setComment(comment);
        slotRepository.save(slot);
    }

    public void unbook(Slot slot) {
        if (slot.getBooker() == null)
            throw new SlotNotBookedException();
        slot.setBooker(null);
        slot.setComment(null);
        slotRepository.save(slot);
    }
}
