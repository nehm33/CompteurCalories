package com.platydev.compteurcalories.infrastructure;

import com.platydev.compteurcalories.dto.input.SigninInput;
import com.platydev.compteurcalories.entity.security.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(java.util.Set.of(new com.platydev.compteurcalories.entity.security.Role(1,com.platydev.compteurcalories.entity.security.AppRole.ROLE_USER)))")
    User toUser(SigninInput signinInput);
}
