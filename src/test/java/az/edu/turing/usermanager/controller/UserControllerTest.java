package az.edu.turing.usermanager.controller;

import az.edu.turing.usermanager.contoller.UserController;
import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.model.dto.UserDto;
import az.edu.turing.usermanager.model.dto.request.CreateUserRequest;
import az.edu.turing.usermanager.model.dto.request.UpdateUserRequest;
import az.edu.turing.usermanager.model.enums.UserStatus;
import az.edu.turing.usermanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static az.edu.turing.usermanager.common.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private static final CreateUserRequest CREATE_USER_REQUEST = new CreateUserRequest(USERNAME, PASSWORD, PASSWORD);
    private static final UpdateUserRequest UPDATE_USER_REQUEST = new UpdateUserRequest(USERNAME, PASSWORD);

    @Test
    void getAll_Should_Return_Success() {
        when(userService.findAll()).thenReturn(Set.of(USER_DTO));
        ResponseEntity<Set<UserDto>> users = userController.getAll();

        assertNotNull(users);
        assertEquals(200, users.getStatusCodeValue());
        assertEquals(Set.of(USER_DTO), users.getBody());

        verify(userService, times(1)).findAll();
    }

    @Test
    void create_Should_Return_Success() {
        when(userService.create(CREATE_USER_REQUEST)).thenReturn(USER_DTO);

        ResponseEntity<UserDto> users = userController.create(CREATE_USER_REQUEST);

        assertNotNull(users);
        assertEquals(201, users.getStatusCodeValue());
        assertEquals(USER_DTO, users.getBody());

        verify(userService, times(1)).create(CREATE_USER_REQUEST);
    }

    @Test
    void getByUsername_Should_Return_Success() {
        when(userService.findByUsername(USERNAME)).thenReturn(USER_DTO);

        ResponseEntity<UserDto> users = userController.getByUsername(USERNAME);

        assertNotNull(users);
        assertEquals(200, users.getStatusCodeValue());
        assertEquals(USER_DTO, users.getBody());

        verify(userService, times(1)).findByUsername(USERNAME);
    }

    @Test
    void update_Should_Return_Success() {
        when(userService.update(ID, UPDATE_USER_REQUEST)).thenReturn(USER_DTO);

        ResponseEntity<UserDto> users = userController.update(ID, UPDATE_USER_REQUEST);

        assertNotNull(users);
        assertEquals(200, users.getStatusCodeValue());
        assertEquals(USER_DTO, users.getBody());

        verify(userService, times(1)).update(ID, UPDATE_USER_REQUEST);
    }

    @Test
    void updateStatus_Should_Return_Success() {
        when(userService.updateStatus(ID, UserStatus.INACTIVE)).thenReturn(USER_DTO);

        ResponseEntity<UserDto> users = userController.updateStatus(ID, UserStatus.INACTIVE);

        assertNotNull(users);
        assertEquals(200, users.getStatusCodeValue());
        assertEquals(USER_DTO, users.getBody());

        verify(userService, times(1)).updateStatus(ID, UserStatus.INACTIVE);
    }

    @Test
    void delete_Should_Return_Success() {
        doNothing().when(userService).deleteById(ID);

        ResponseEntity<UserDto> users = userController.delete(ID);

        assertNotNull(users);
        assertEquals(204, users.getStatusCodeValue());
        assertNull(users.getBody());

        verify(userService, times(1)).deleteById(ID);
    }
}
