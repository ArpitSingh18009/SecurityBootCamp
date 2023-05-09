package com.example.springsecuritydailycode.Repository;

import com.example.springsecuritydailycode.Entity.User;
import com.example.springsecuritydailycode.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailId(String email);
}
