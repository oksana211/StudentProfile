package com.example.StudentProfile.controllers;


import com.example.StudentProfile.models.User;
import com.example.StudentProfile.models.UserInfo;
import com.example.StudentProfile.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1/users")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class.getSimpleName());

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserServiceImpl customUserDetailsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServiceImpl.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getProfile(@PathVariable("id") final Long id,
                                             @RequestHeader("Authorization") String header) throws Exception {

        logger.info("yout header is OK with id" + header);
        if(customUserDetailsService.compareId(header, id)){
            return new ResponseEntity<User>(userServiceImpl.getUserById(id).get(), HttpStatus.OK);
        }
        else{
            throw new Exception("dont have access");
        }
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

    @PatchMapping(path = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE,
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") final Long id,
                                                  @RequestHeader("Authorization") String header) throws Exception {

        if(customUserDetailsService.compareId(header, id)){
            userServiceImpl.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();        }
        else{
            throw new Exception("dont have access");
        }
//        userServiceImpl.deleteUser(id);
//        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
