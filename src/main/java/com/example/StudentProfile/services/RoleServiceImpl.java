package com.example.StudentProfile.services;

import com.example.StudentProfile.models.Role;
import com.example.StudentProfile.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findRoleById(Long id){
        return roleRepository.findById(id);
    }

}
