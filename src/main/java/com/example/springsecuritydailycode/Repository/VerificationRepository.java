package com.example.springsecuritydailycode.Repository;

import com.example.springsecuritydailycode.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken,Long> {
    public VerificationToken findByToken(String token);
}
