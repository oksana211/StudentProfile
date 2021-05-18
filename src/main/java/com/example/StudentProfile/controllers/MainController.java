package com.example.StudentProfile.controllers;


import com.example.StudentProfile.dto.PersonalStatistic;
import com.example.StudentProfile.dto.Statistic;
import com.example.StudentProfile.models.User;
import com.example.StudentProfile.models.UserInfo;
import com.example.StudentProfile.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.example.StudentProfile.services.UserServiceImpl.backup2;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1/")
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class.getSimpleName());

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserServiceImpl customUserDetailsService;

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("coool");
        return new ResponseEntity<>(userServiceImpl.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student')")
    @GetMapping(path = "users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getProfile(@PathVariable("id") final Long id,
                                             @RequestHeader("Authorization") String header) throws Exception {

        logger.info("yout header is OK with id" + header);
        if(customUserDetailsService.isAdmin(header)){
            return new ResponseEntity<User>(userServiceImpl.getUserById(id).get(), HttpStatus.OK);
        }
        if(customUserDetailsService.compareId(header, id)){
            return new ResponseEntity<User>(userServiceImpl.getUserById(id).get(), HttpStatus.OK);
        }
        else{
            throw new Exception("dont have access");
        }
    }

    @PreAuthorize("hasAuthority('student')")
    @PatchMapping(path = "users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateProfile(@RequestBody final UserInfo userUpdate,
                                              @PathVariable("id") final Long id,
                                              @RequestHeader("Authorization") String header) throws Exception {

        if(customUserDetailsService.compareId(header, id)){
            User user = userServiceImpl.getUserById(id).get();

            UserInfo userInfo = userServiceImpl.getUserInfo(user);
            if (userUpdate.getName() != null) {
                userInfo.setName(userUpdate.getName());
            }
            if (userUpdate.getSurname() != null) {
                userInfo.setSurname(userUpdate.getSurname());
            }
            if (userUpdate.getPhone_number() != null) {
                userInfo.setPhone_number(userUpdate.getPhone_number());
            }
            if (userUpdate.getSchool() != null) {
                userInfo.setSchool(userUpdate.getSchool());
            }

            userServiceImpl.saveUserInfo(userInfo);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else{
            throw new Exception("dont have access");
        }
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student')")
    @DeleteMapping(path = "users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") final Long id,
                                                  @RequestHeader("Authorization") String header) throws Exception {

        if(customUserDetailsService.isAdmin(header)){
            userServiceImpl.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        if(customUserDetailsService.compareId(header, id)){
            userServiceImpl.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else{
            throw new Exception("dont have access");
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistic> getStatistic() {
        logger.info("start statistic");
        return new ResponseEntity<>(userServiceImpl.getStatistic(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/statistic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonalStatistic> getPersonalStatistic(@PathVariable("id") final Long id) {
        logger.info("start personal statistic");
        return new ResponseEntity<>(userServiceImpl.getPersonalStatistic(userServiceImpl.getUserById(id).get()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/backup")
    public ResponseEntity<Void> backupSQL() throws IOException, InterruptedException, URISyntaxException {
        backup2();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<User>> findUsers(@RequestHeader("Authorization") String header) throws Exception {
//        logger.info("yout header is OK" + header);
//        return new ResponseEntity<>(userServiceImpl.findAll(), HttpStatus.OK);
//    }

//    @GetMapping(path="/user", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
//        return new ResponseEntity<User>(userServiceImpl.getUserById(id).get(), HttpStatus.OK);
//    }


//    @PostMapping(path = "/users",consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> createNewUser(@RequestBody User user)
//            throws URISyntaxException {
//        userServiceImpl.save(user);
//        return ResponseEntity.status(HttpStatus.OK).build();
////        return ResponseEntity.created(new URI("/users/" + createdUser
////                .getId())).body(user);
//
//    }

//    @PutMapping(path="users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> replaceUser(@RequestBody final User newProduct,
//                                                  @PathVariable("id") final Long id) {
//        return userServiceImpl.getUserById(id)
//                .map(product -> {
//                    product.setLogin(newProduct.getLogin());
//                    return ResponseEntity.ok(userServiceImpl.saveUser(product));
//                })
//                .orElseGet(() -> {
//                    newProduct.setId(id);
//                    return ResponseEntity.ok(userServiceImpl.saveUser(newProduct));
//                });
//    }



}
