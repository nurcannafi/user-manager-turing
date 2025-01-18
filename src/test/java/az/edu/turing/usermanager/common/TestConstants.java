package az.edu.turing.usermanager.common;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.model.dto.UserDto;
import az.edu.turing.usermanager.model.dto.request.CreateUserRequest;
import az.edu.turing.usermanager.model.dto.request.UpdateUserRequest;
import az.edu.turing.usermanager.model.enums.UserStatus;

public interface TestConstants {

    Long ID = 1L;
    String USERNAME = "admin@turing.com";
    String PASSWORD = "Admin123!";

    CreateUserRequest CREATE_USER_REQUEST = new CreateUserRequest(USERNAME, PASSWORD, PASSWORD);
    UpdateUserRequest UPDATE_USER_REQUEST = new UpdateUserRequest(USERNAME, PASSWORD);

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(ID)
            .username(USERNAME)
            .password(PASSWORD)
            .status(UserStatus.ACTIVE)
            .build();

    UserDto USER_DTO = UserDto.builder()
            .id(ID)
            .username(USERNAME)
            .password(PASSWORD)
            .status(UserStatus.ACTIVE)
            .build();
}
