package com.l3info.prdv.group;

import com.l3info.prdv.group.exception.GroupAlreadyExistsException;
import com.l3info.prdv.group.exception.GroupNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // ---

    public Group create(Group group) {
        if (groupRepository.existsByName(group.getName()))
            throw new GroupAlreadyExistsException();
        return groupRepository.save(group);
    }

    public void delete(Group group) {
        if (!groupRepository.existsById(group.getId()))
            throw new GroupNotFoundException();
        groupRepository.delete(group);
    }

    public Group findOne(Long id) {
        return groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }

    public Group findOne(String name) {
        return groupRepository.findByName(name).orElseThrow(GroupNotFoundException::new);
    }

    @Transactional
    public Group findOneWithMembers(Long id) {
        Group group = findOne(id);
        Hibernate.initialize(group.getAccounts());
        return group;
    }

    public List<Group> findAll() {
        return StreamSupport.stream(groupRepository.findAll().spliterator(), true).collect(Collectors.toList());
    }

    public List<Group> findAll(Collection<Group> excluded) {
        List<Group> groups = findAll();
        groups.removeAll(excluded);
        return groups;
    }

    @Transactional
    public List<Group> findAllWithMembers() {
        List<Group> groups = findAll();
        groups.forEach(g -> Hibernate.initialize(g.getAccounts()));
        return groups;
    }

    public void update(Group group, Group data) {
        if (groupRepository.existsByIdNotAndName(group.getId(), data.getName()))
            throw new GroupAlreadyExistsException();
        group.setName(data.getName());
        groupRepository.save(group);
    }

    public Group findGroupByName(String s) {
        List<Group> groups = StreamSupport.stream(groupRepository.findAll().spliterator(), true).toList();
        for (Group g : groups) {
            if (s.equals(g.getName())) {
                return g;
            }
        }
        return null;
    }
}
