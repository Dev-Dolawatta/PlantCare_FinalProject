package lk.ijse.plantcareapplication.controller;

import jakarta.servlet.http.HttpSession;
import lk.ijse.plantcareapplication.entity.Users;
import lk.ijse.plantcareapplication.repository.UsersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@CrossOrigin
public class LoginController {


    private final UsersRepository usersRepository;

    public LoginController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        session.setAttribute("loggedUser", user);

        return "redirect:/new.html"; // works correctly with @Controller
    }
}
