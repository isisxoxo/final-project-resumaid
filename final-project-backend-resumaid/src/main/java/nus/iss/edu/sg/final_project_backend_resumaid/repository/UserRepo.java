package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import nus.iss.edu.sg.final_project_backend_resumaid.model.User;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.Queries;

@Repository
public class UserRepo implements Queries {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Check if email already exists
    public Boolean emailExist(String email) {

        Boolean isEmailExist = false;

        SqlRowSet rs = jdbcTemplate.queryForRowSet(COUNT_USER_BY_EMAIL, email);
        if (rs.next()) {
            int x = rs.getInt("count");
            if (x > 0) {
                isEmailExist = true;
            }
        }
        return isEmailExist;
    }

    // Insert new user (Registration)
    public String saveUser(User newUser) {

        String id = UUID.randomUUID().toString().substring(0, 8);

        jdbcTemplate.update(INSERT_USER, id, newUser.getUsername(), newUser.getEmail(), newUser.getPassword());

        return id;
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_EMAIL, email);
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = new User();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(email);
        user.setPassword(rs.getString("password"));
        return Optional.of(user);
    }

    // Get user by id
    public Optional<User> getUserById(String id) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_ID, id);
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = new User();
        user.setId(id);
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return Optional.of(user);
    }
}
