package az.edu.turing.usermanager.mapper;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.model.enums.UserStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEntity.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .status(UserStatus.valueOf(rs.getString("status")))
                .build();
    }
}
