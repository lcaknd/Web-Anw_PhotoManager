package com.example.demo.repository;

import com.example.demo.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);

    @Query(value = "select * from image_data where name like %:fileName%", nativeQuery = true)
    List<ImageData> findAllByNameLike(@Param("fileName") String fileName);
}

