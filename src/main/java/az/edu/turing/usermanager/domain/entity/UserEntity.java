package az.edu.turing.usermanager.domain.entity;

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
public class UserEntity {

    private Long id;

    @Email
    private String username;
    private String password;
    private UserStatus status;
}
