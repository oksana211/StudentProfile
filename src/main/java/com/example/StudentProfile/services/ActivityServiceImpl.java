package com.example.StudentProfile.services;

import com.example.StudentProfile.models.Activity;
import com.example.StudentProfile.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> findActivityBySessionId(Long id){
        return activityRepository.findActivityBySessionId(id);
    }

}
