package com.hubt.data.repository;

import com.hubt.data.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.userId = :userId and i.post is null ")
    List<Image> findByUserId(@Param("userId") long userId);

    List<Image> findByPostId(long postId);
}
