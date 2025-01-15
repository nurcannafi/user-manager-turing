package az.edu.turing.usermanager.service;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.domain.repository.UserRepository;
import az.edu.turing.usermanager.exception.AlreadyExistsException;
import az.edu.turing.usermanager.exception.InvalidInputException;
import az.edu.turing.usermanager.exception.NotFoundException;
import az.edu.turing.usermanager.mapper.UserMapper;
import az.edu.turing.usermanager.model.dto.UserDto;
import az.edu.turing.usermanager.model.dto.request.CreateUserRequest;
import az.edu.turing.usermanager.model.dto.request.UpdateUserRequest;
import az.edu.turing.usermanager.model.enums.UserStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public Set<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto create(CreateUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords don't match");
        }
        existsByUsername(request.getUsername());
        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .status(UserStatus.ACTIVE)
                .build();
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedUserEntity);
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User with username" + username + " not found"));

    }

    public UserDto update(Long id, UpdateUserRequest request) {
        existsByUsername(request.getUsername());
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id" + id + " not found")
        );
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(updatedUserEntity);
    }

    public UserDto updateStatus(Long id, @Valid UserStatus status) {
        return userRepository.findById(id).map(userEntity -> {
                    userEntity.setStatus(status);
                    UserEntity updatedUserEntity = userRepository.save(userEntity);
                    return userMapper.toDto(updatedUserEntity);
                })
                .orElseThrow(() -> new NotFoundException("User with id" + id + " not found"));
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id" + id + " not found");
        }
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus(UserStatus.DELETED);
            userRepository.save(user);
        });
        userRepository.deleteById(id);
    }

    private void existsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("Username already exists");
        }
    }
}
