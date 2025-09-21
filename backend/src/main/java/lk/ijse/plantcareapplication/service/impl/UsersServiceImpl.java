package lk.ijse.plantcareapplication.service.impl;

import lk.ijse.plantcareapplication.entity.Users;
import lk.ijse.plantcareapplication.repository.UsersRepository;
import lk.ijse.plantcareapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Users user) {
        Users existing = usersRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + user.getUserId()));
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setIsActive(user.getIsActive());
        existing.setPlantLimit(user.getPlantLimit());
        System.out.println("Updating user: " + existing.getUserId());
        return usersRepository.save(existing);

    }

    @Override
    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

    @Override
    public Optional<Users> getUserById(Long userId) {
        return usersRepository.findById(userId);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Optional<Users> getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}
