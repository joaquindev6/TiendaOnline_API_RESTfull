package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.userDataHistory.state = :state WHERE u.id = :id")
    void updateState(@Param("state") Byte state, @Param("id") Long id);

    Optional<User> findByUsername(String username);
}
