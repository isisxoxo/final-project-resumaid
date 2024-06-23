package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nus.iss.edu.sg.final_project_backend_resumaid.exceptions.EmailExistsException;
import nus.iss.edu.sg.final_project_backend_resumaid.model.User;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.UserRepo;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // C
    public Map<String, String> registerNewUserAccount(User user) throws EmailExistsException {

        if (userRepo.emailExist(user.getEmail())) {
            // Email already exists
            throw new EmailExistsException(
                    "There is already an existing account with this email address: " + user.getEmail());
        }
        // Email does not exist yet, to create new user
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        String id = userRepo.saveUser(newUser);

        Map<String, String> result = new HashMap<>();
        result.put("jwt", jwtUtil.generateToken(newUser.getEmail(), id));
        result.put("id", id);

        return result;
    }

    // R
    public Map<String, String> getUserByEmail(String email, String password) throws LoginException {

        Optional<User> user = userRepo.getUserByEmail(email);

        if (user.isPresent()) {
            // Email exists, to check for correct password
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                // If password is also correct

                Map<String, String> result = new HashMap<>();
                result.put("jwt", jwtUtil.generateToken(email, user.get().getId()));
                result.put("id", user.get().getId());
                return result;
            }
        }
        // Login email and/or password is wrong
        throw new LoginException("Email and/or Password is incorrect. Please try again.");
    }
}
