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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static az.edu.turing.usermanager.common.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_Should_Return_Success() {
        when(userRepository.findAll()).thenReturn(Set.of(USER_ENTITY));
        doReturn(USER_DTO).when(userMapper).toDto(USER_ENTITY);

        Set<UserDto> users = userService.findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(USER_DTO, users.iterator().next());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(USER_ENTITY);
    }

    @Test
    void create_Should_Return_Success() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER_ENTITY);
        doReturn(USER_DTO).when(userMapper).toDto(USER_ENTITY);

        UserDto result = userService.create(new CreateUserRequest(USERNAME, PASSWORD, PASSWORD));

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        verify(userRepository, times(1)).existsByUsername(USERNAME);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toDto(USER_ENTITY);
    }

    @Test
    void create_Should_Throws_Exception_When_PasswordDoesNotMatch() {
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            userService.create(new CreateUserRequest(USERNAME, "Password@", "Password"));
        });

        assertEquals("Passwords don't match", exception.getMessage());
        verify(userRepository, never()).existsByUsername(USERNAME);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void create_Should_Throws_exception_When_UsernameAlreadyExists() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {userService.create(
                new CreateUserRequest(USERNAME, PASSWORD, PASSWORD));
        });

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(USERNAME);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void findByUsername_Should_Return_Success() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER_ENTITY));
        doReturn(USER_DTO).when(userMapper).toDto(USER_ENTITY);

        UserDto result = userService.findByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(userMapper, times(1)).toDto(USER_ENTITY);
    }

    @Test
    void findByUsername_Should_Throws_Exception_When_UserNotFoundByUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByUsername(USERNAME));

        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(userMapper, never()).toDto(USER_ENTITY);
    }

    @Test
    void updateUser_Should_Return_Success() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER_ENTITY));
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER_ENTITY);
        doReturn(USER_DTO).when(userMapper).toDto(USER_ENTITY);

        UserDto result = userService.update(ID, new UpdateUserRequest(USERNAME, PASSWORD));

        assertNotNull(result);
        assertEquals(USER_DTO, result);

        verify(userRepository, times(1)).existsByUsername(USERNAME);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toDto(USER_ENTITY);
    }

    @Test
    void updateUser_Should_Throws_Exception_When_UserNotFoundById() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(ID, new UpdateUserRequest(USERNAME, PASSWORD)));

        verify(userRepository, times(1)).existsByUsername(USERNAME);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(userMapper, never()).toDto(USER_ENTITY);
    }

    @Test
    void updateStatus_Should_Return_Success() {
        UserEntity updatedUserEntity = UserEntity.builder()
                .id(USER_ENTITY.getId())
                .username(USER_ENTITY.getUsername())
                .password(USER_ENTITY.getPassword())
                .status(UserStatus.INACTIVE)
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(updatedUserEntity.getId())
                .username(updatedUserEntity.getUsername())
                .password(updatedUserEntity.getPassword())
                .status(updatedUserEntity.getStatus())
                .build();

        when(userRepository.findById(ID)).thenReturn(Optional.of(USER_ENTITY));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUserEntity);
        doReturn(updatedUserDto).when(userMapper).toDto(updatedUserEntity);

        UserDto result = userService.updateStatus(ID, UserStatus.INACTIVE);

        assertNotNull(result);
        assertEquals(updatedUserDto, result);

        verify(userRepository, times(1)).findById(ID);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toDto(updatedUserEntity);
    }

    @Test
    void updateStatus_Should_Throws_Exception_When_UserNotFoundById() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.updateStatus(ID,
                UserStatus.INACTIVE));

        assertEquals("User with id" + ID + " not found", exception.getMessage());

        verify(userRepository, times(1)).findById(ID);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(userMapper, never()).toDto(USER_ENTITY);
    }

    @Test
    void deleteById_Should_Return_Success() {
        when(userRepository.existsById(ID)).thenReturn(true);

        userService.deleteById(ID);

        verify(userRepository, times(1)).deleteById(ID);
        verify(userRepository, times(1)).existsById(ID);
    }

    @Test
    void deleteById_Should_Throws_Exception_When_UserNotFoundById() {
        when(userRepository.existsById(ID)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteById(ID));

        assertEquals("User with id" + ID + " not found", exception.getMessage());

        verify(userRepository, times(1)).existsById(ID);
        verify(userRepository, never()).deleteById(ID);
    }

}
