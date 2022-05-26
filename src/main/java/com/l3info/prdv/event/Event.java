package com.l3info.prdv.event;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.group.Group;
import com.l3info.prdv.slot.Slot;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Event implements Comparable<Event> {

    public static final int NAME_LENGTH = 64;
    public static final int DESC_LENGTH = 1500;
    public static final int LOCA_LENGTH = 64;

    // ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = NAME_LENGTH, nullable = false, unique = true)
    private String name;

    @Lob
    private String description;

    @Column(length = LOCA_LENGTH)
    private String location;

    @ManyToOne(optional = false)
    @Fetch(FetchMode.JOIN)
    private Account author;

    @ManyToMany(mappedBy = "events")
    private Set<Group> groups;

    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private Set<Slot> slots;

    // ---

    @Override
    public int compareTo(Event o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
