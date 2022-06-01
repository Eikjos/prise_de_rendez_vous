package com.l3info.prdv.account;

import com.l3info.prdv.event.Event;
import com.l3info.prdv.group.Group;
import com.l3info.prdv.slot.Slot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Account implements Comparable<Account> {

    public static final int EMAIL_LENGTH = 64;
    public static final int LOGIN_LENGTH = 8;
    public static final int NAMES_LENGTH = 32;

    // ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CHAR(" + LOGIN_LENGTH + ")", nullable = false, unique = true)
    private String username;

    @Column(columnDefinition = "BINARY(60)", nullable = false)
    private String password;

    @Column(length = NAMES_LENGTH, nullable = false)
    private String firstName;

    @Column(length = NAMES_LENGTH, nullable = false)
    private String lastName;

    @Column(length = EMAIL_LENGTH, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private AccountType type;

    @ManyToMany(mappedBy = "accounts")
    private Set<Group> groups;

    @OneToMany(mappedBy = "booker")
    private Set<Slot> booked;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Event> events;

    // ---

    @Override
    public int compareTo(Account o) {
        int delta = lastName.compareTo(o.lastName);
        if (delta != 0)
            return delta;
        return firstName.compareTo(o.firstName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String exportString() {
        return username + ";" + password + ";" + firstName + ";" + lastName + ";" + email + ";" + type.toString() + ";"
                + groups.toString();
    }
}
