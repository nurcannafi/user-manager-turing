package az.edu.turing.usermanager.common;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.model.dto.UserDto;
import az.edu.turing.usermanager.model.enums.UserStatus;

public interface TestConstants {

    Long ID = 1L;
    String USERNAME = "admin@turing.com";
    String PASSWORD = "Admin123!";

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
