package com.company.ems.Repository;
import com.company.ems.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    long countByRole(String role);
    long countByRoleAndStatus(String role, String status);
    List<User> findAllByRole(String role);
}
