package com.smartpos.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @JoinColumn(name = "branch_id")
//    private List<User> employees;
//
//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @JoinColumn(name = "branch_id")
//    private List<Order> orders;
}
