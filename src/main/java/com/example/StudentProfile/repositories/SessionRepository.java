package com.example.StudentProfile.repositories;

import com.example.StudentProfile.models.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    @Query(value = "SELECT * FROM sessions WHERE sessions.user_id = ? ORDER BY sessions.date DESC",
            nativeQuery = true)
    List<Session> findSessionByUserId(Long id);
}
