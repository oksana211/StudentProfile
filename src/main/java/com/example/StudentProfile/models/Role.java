package com.example.StudentProfile.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role")
    private String role;

//    @OneToMany(mappedBy = "role")
//    private List<User> users;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String role) {
        this.role = role;
    }

    public String getName() {
        return role;
    }
}
