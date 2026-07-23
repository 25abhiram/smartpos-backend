package com.smartpos.backend.component;

import com.smartpos.backend.entity.ERole;
import com.smartpos.backend.entity.Permission;
import com.smartpos.backend.entity.Role;
import com.smartpos.backend.repository.PermissionRepository;
import com.smartpos.backend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {
    private final RoleRepository roleRepository;
//    private final PermissionRepository permissionRepository;

//    public DataInitializer(RoleRepository roleRepository,PermissionRepository permissionRepository){
//        this.roleRepository=roleRepository;
//        this.permissionRepository=permissionRepository;
//    }
    public DataInitializer(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @PostConstruct
    public void seedRolesAndPermissions(){
        if (roleRepository.findByName(ERole.ADMIN).isPresent())
            return;

//        Permission readUser=permissionRepository.save(new Permission(null,"READ_USER"));
//        Permission deletePost=permissionRepository.save(new Permission(null,"DELETE_POST"));

//        Role admin=new Role();
//        admin.setName(ERole.ADMIN);
//        admin.setPermissions(Set.of(readUser,deletePost));
//        roleRepository.save(admin);
//
//        Role cashier=new Role();
//        cashier.setName(ERole.CASHIER);
//        roleRepository.save(cashier);
        roleRepository.save(new Role(null,ERole.ADMIN));
        roleRepository.save(new Role(null,ERole.CASHIER));
    }
}
