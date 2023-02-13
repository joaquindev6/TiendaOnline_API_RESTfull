package com.jfarro.app.repositories;

import com.jfarro.app.models.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Modifying
    @Query("UPDATE SubCategory s SET s.userDataHistory.state = :state WHERE s.id = :id")
    void updateState(@Param("state") Byte state, @Param("id") Long id);
}
