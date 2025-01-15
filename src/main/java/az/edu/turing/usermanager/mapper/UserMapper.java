package az.edu.turing.usermanager.mapper;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.model.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .build();
    }

    public UserEntity toEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .status(userDto.getStatus())
                .build();
    }
}
