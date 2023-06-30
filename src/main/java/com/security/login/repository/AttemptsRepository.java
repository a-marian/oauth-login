package com.security.login.repository;

import com.security.login.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttemptsRepository extends JpaRepository<Attempt, Integer> {

    Optional<Attempt> findAttemptsByUsername(String username);
}
