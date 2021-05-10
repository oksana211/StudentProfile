package com.example.StudentProfile.repositories;

import com.example.StudentProfile.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

    @Query(value = "SELECT info_id FROM users WHERE users.id = ? ",
            nativeQuery = true)
    Long findUserInfoIdByUserId(Long id);

}
