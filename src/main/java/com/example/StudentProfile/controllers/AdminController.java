package com.example.StudentProfile.controllers;

import com.example.StudentProfile.dto.Statistic;
import com.example.StudentProfile.models.User;
import com.example.StudentProfile.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.example.StudentProfile.services.UserServiceImpl.backup2;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class.getSimpleName());

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServiceImpl.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistic> getStatistic() {
        logger.info("start statistic");
        return new ResponseEntity<>(userServiceImpl.getStatistic(), HttpStatus.OK);
    }

    @GetMapping(path="/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
        return new ResponseEntity<User>(userServiceImpl.getUserById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(path="/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") final Long id) {
        logger.info("id " + id);
        userServiceImpl.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "/backup")
    public ResponseEntity<Void> backupSQL() throws IOException, InterruptedException, URISyntaxException {
        backup2();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
