package az.edu.turing.usermanager.domain.repository;

import az.edu.turing.usermanager.domain.entity.UserEntity;
import az.edu.turing.usermanager.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Set<UserEntity> findAll() {
        String sql = "SELECT * FROM users";
        return new HashSet<>(jdbcTemplate.query(sql, new UserRowMapper()));
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, password, status) VALUES (?, ?, ?) RETURNING id";
            Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                    user.getUsername(),
                    user.getPassword(),
                    user.getStatus());
            user.setId(generatedId);
        } else {
            String sql = "UPDATE users SET username = ?, password = ?, status = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getStatus(), user.getId());
        }
        return user;
    }

    public Optional<UserEntity> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, new UserRowMapper())
                .stream()
                .findFirst();
    }

    public Optional<UserEntity> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, new UserRowMapper())
                .stream()
                .findFirst();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
