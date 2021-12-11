package com.brew_tracker.user.model;

import com.brew_tracker.user.persistence.entity.Role;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class UserDto {


    private String username;

    private String email;

    private String password;

    List<Role> roles;
}
