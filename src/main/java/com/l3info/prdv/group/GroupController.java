package com.l3info.prdv.group;

import com.l3info.prdv.group.dto.CreateGroupDto;
import com.l3info.prdv.group.dto.UpdateGroupDto;
import com.l3info.prdv.group.exception.GroupAlreadyExistsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // ---

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "group/list";
    }

    // ---

    @GetMapping("/create")
    public String create(@ModelAttribute("createGroupDto") CreateGroupDto createGroupDto) {
        return "group/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("createGroupDto") CreateGroupDto createGroupDto,
                         BindingResult result) {
        if (result.hasErrors())
            return "group/create";
        try {
            Group group = groupService.create(createGroupDto.toGroup());
            return "redirect:/group/" + group.getId();
        } catch (GroupAlreadyExistsException ex) {
            result.rejectValue("name", "", "Un groupe existe déjà avec ce nom");
            return "group/create";
        }
    }

    // ---

    @GetMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @ModelAttribute("updateGroupDto") UpdateGroupDto updateGroupDto,
                         Model model) {
        Group group = groupService.findOneWithMembers(id);
        updateGroupDto.setName(group.getName());
        model.addAttribute("group", group);
        return "group/manage";
    }

    @PostMapping("/{id}")
    public String manage(@PathVariable Long id,
                         @Valid @ModelAttribute UpdateGroupDto updateGroupDto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("group", groupService.findOneWithMembers(id));
            return "group/manage";
        }
        try {
            Group group = groupService.findOne(id);
            groupService.update(group, updateGroupDto.toGroup());
        } catch (GroupAlreadyExistsException ex) {
            result.rejectValue("name", "", "Ce nom est déjà utilisé par un autre groupe");
            model.addAttribute("group", groupService.findOneWithMembers(id));
            return "group/manage";
        }
        return "redirect:/group/{groupId}?updated";
    }

    // ---

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Group group = groupService.findOne(id);
        groupService.delete(group);
        return "redirect:/group/list?deleted";
    }
}
