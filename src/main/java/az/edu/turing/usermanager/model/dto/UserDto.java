package az.edu.turing.usermanager.model.dto;

import az.edu.turing.usermanager.model.enums.UserStatus;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @Email
    private String username;
    private String password;
    private UserStatus status;
}
