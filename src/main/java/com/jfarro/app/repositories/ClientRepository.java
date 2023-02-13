package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Modifying
    @Query("UPDATE Client c SET c.userDataHistory.state = :state WHERE c.id = :id")
    void updateState(@Param("state") Byte state, @Param("id") Long id);
}
