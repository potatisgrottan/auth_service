package com.example.auth_service.core;


import com.example.auth_service.enums.HospitalRole;
import com.example.auth_service.db.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    /*@Transactional
    public User createUser(String email, String password, HospitalRole role) {
        return createUser(email, password, null, role);
    }*/

    @Transactional
    public User register(String email, String password, String fullName, HospitalRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRole(role);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User authenticate(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

    /*@Transactional(readOnly = true)
    public List<User> findAvailableUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAllPractitioners() {
        return userRepository.findAll()
                .stream()
                .filter(u ->
                        "DOCTOR".equalsIgnoreCase(u.getRole()) ||
                                "NURSE".equalsIgnoreCase(u.getRole())
                )
                .toList();
    }

    public List<User> findUsersAvailableToMessage(User currentUser) {
        switch (currentUser.getRole()) {
            case HospitalRole.PATIENT:
                return userRepository.findByRoleIn(List.of("DOCTOR", "NURSE"));

            case HospitalRole.DOCTOR:
            case HospitalRole.NURSE:
                return userRepository.findByRole(HospitalRole.PATIENT);

            default:
                return List.of();
        }
    }*/

}