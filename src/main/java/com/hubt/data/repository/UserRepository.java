package com.hubt.data.repository;

import com.hubt.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String username);

    @Query("select u from User u where u.username like concat('%', :searchKey,'%') " +
            "or u.phoneNumber like concat ('%',:searchKey,'%') ")
    Page<User> getUserByUsernameOrPhoneNumber(@Param("searchKey") String searchKey, Pageable pageable);

    Optional<User> findByFacebookId(long facebookId);
}
