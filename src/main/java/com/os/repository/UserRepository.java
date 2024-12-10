package com.os.repository;

import com.os.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByAccountId(String accountId);
    Optional<User> findByAccountIdAndStatus(String accountId, String status);

}
