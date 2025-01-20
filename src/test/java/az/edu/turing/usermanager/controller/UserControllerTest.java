package az.edu.turing.usermanager.controller;

import az.edu.turing.usermanager.contoller.UserController;
import az.edu.turing.usermanager.model.enums.UserStatus;
import az.edu.turing.usermanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static az.edu.turing.usermanager.common.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getAll_Should_Return_Success() throws Exception {
        when(userService.findAll()).thenReturn(Set.of(USER_DTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Set.of(USER_DTO))));

        verify(userService, times(1)).findAll();
    }

    @Test
    void create_Should_Return_Success() throws Exception {
        when(userService.create(CREATE_USER_REQUEST)).thenReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_USER_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));

        verify(userService, times(1)).create(CREATE_USER_REQUEST);
    }

    @Test
    void getByUsername_Should_Return_Success() throws Exception {
        when(userService.findByUsername(USERNAME)).thenReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));

        verify(userService, times(1)).findByUsername(USERNAME);
    }

    @Test
    void update_Should_Return_Success() throws Exception {
        when(userService.update(ID, UPDATE_USER_REQUEST)).thenReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));

        verify(userService, times(1)).update(ID, UPDATE_USER_REQUEST);
    }

    @Test
    void updateStatus_Should_Return_Success() throws Exception {
        when(userService.updateStatus(ID, UserStatus.INACTIVE)).thenReturn(USER_DTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", ID)
                        .param("status", UserStatus.INACTIVE.name()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)));

        verify(userService, times(1)).updateStatus(ID, UserStatus.INACTIVE);
    }

    @Test
    void delete_Should_Return_Success() throws Exception {
        doNothing().when(userService).deleteById(ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteById(ID);
    }
}
