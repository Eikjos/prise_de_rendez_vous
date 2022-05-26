package com.l3info.prdv.group;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Long> {

    boolean existsByName(String name);

    boolean existsByIdNotAndName(Long id, String name);

    Optional<Group> findByName(String name);
}
