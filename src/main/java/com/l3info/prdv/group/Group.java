package com.l3info.prdv.group;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.event.Event;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Group implements Comparable<Group> {

    public static final int NAME_LENGTH = 64;

    // ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = NAME_LENGTH, nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable
    private Set<Account> accounts;

    @ManyToMany
    @JoinTable
    private Set<Event> events;

    // ---

    @Override
    public int compareTo(Group o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id);
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
