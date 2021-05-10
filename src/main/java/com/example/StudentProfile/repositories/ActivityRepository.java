package com.example.StudentProfile.repositories;

import com.example.StudentProfile.models.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    @Query(value = "SELECT * FROM activities WHERE activities.session_id = ?",
            nativeQuery = true)
    List<Activity> findActivityBySessionId(Long id);
}
