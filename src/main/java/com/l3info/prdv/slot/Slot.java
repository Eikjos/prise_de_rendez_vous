package com.l3info.prdv.slot;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.Event;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Slot implements Comparable<Slot> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @Fetch(FetchMode.JOIN)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime start;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private Account booker;

    @Lob
    private String comment;

    // ---

    @Override
    public int compareTo(Slot o) {
        return start.compareTo(o.start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, start);
    }

    @Override
    public String toString() {
        return event + " " + start.toString();
    }
}
