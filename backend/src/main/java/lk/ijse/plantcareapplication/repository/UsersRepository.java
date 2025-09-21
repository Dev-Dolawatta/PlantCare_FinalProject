package lk.ijse.plantcareapplication.repository;

import lk.ijse.plantcareapplication.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users>findByEmail(String email);

}
