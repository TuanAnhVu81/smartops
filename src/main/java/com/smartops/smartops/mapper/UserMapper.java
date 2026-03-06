package com.smartops.smartops.mapper;

import com.smartops.smartops.dto.response.UserResponse;
import com.smartops.smartops.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "role.roleName", target = "role")
    UserResponse toResponse(User user);
}
