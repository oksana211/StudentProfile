package com.example.StudentProfile.services;

import com.example.StudentProfile.models.Session;
import com.example.StudentProfile.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl {

    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> findAll(){
        return (List<Session>) sessionRepository.findAll();
    }

    public List<Session> findSessionByUserId(Long id){
        return sessionRepository.findSessionByUserId(id);
    }
}
