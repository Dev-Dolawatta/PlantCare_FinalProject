package lk.ijse.plantcareapplication.service;

import lk.ijse.plantcareapplication.entity.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public interface UsersService {
    Users saveUser(Users user);
    Users updateUser(Users user);
    void deleteUser(Long userId);
    Optional<Users> getUserById(Long userId);
    List<Users> getAllUsers();
    Optional<Users> getUserByUsername(String username);
    Optional<Users> getUserByEmail(String email);
}
