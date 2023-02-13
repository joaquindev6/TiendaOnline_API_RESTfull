package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Query("UPDATE Role r SET r.userDataHistory.state = :state WHERE r.id = :id")
    void updateState(@Param("state") Byte state, @Param("id") Long id);
}
