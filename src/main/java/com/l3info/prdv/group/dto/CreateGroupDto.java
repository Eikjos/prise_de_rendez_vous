package com.l3info.prdv.group.dto;

import com.l3info.prdv.group.Group;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateGroupDto {

    @NotBlank(message = "Le nom du groupe est obligatoire")
    @Size(max = Group.NAME_LENGTH, message = "Le nom du groupe ne doit excéder " + Group.NAME_LENGTH + " caractères")
    private String name;

    // ---

    public Group toGroup() {
        Group group = new Group();
        group.setName(name);
        return group;
    }
}
