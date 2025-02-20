package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id
            WHERE u.id = :userId AND (t.expired = false AND t.revoked = false)""")
    Optional<Token> findValidTokenByUserId(Long userId);

    @Modifying
    @Query("""
            DELETE FROM Token t WHERE t.expired = true OR t.revoked = true""")
    void deleteRevokedTokens();

}