package com.smartpos.backend.service;

import com.smartpos.backend.dto.UpdateUserRequest;
import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.entity.Role;
import com.smartpos.backend.entity.User;
import com.smartpos.backend.exceptions.DuplicateResourceException;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.BranchRepository;
import com.smartpos.backend.repository.RoleRepository;
import com.smartpos.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers(){
        List<User> users=userRepository.findAll();
        return users;
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
    }

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserRequest userRequest){
        User userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));

        String newUsername=userRequest.getUsername();
        if (newUsername!=null && !newUsername.trim().isEmpty() && !newUsername.equals(userData.getUsername())){
            if (userRepository.existsByUsername(newUsername)){
                throw new DuplicateResourceException("Username '"+newUsername+"' is already taken.");
            }
            userData.setUsername(newUsername);
        }

        String newPassword=userRequest.getPassword();
        if (newPassword!=null && !newPassword.trim().isEmpty()){
            userData.setPassword(passwordEncoder.encode(newPassword));
        }

        Long newBranchId= userRequest.getBranchId();
        if (newBranchId!=null){
            Branch existingBranch=branchRepository.findById(newBranchId)
                    .orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+newBranchId));
            userData.setBranch(existingBranch);
        }

        Set<Long> newRoleIds=userRequest.getRoleIds();
        if (newRoleIds!=null && !newRoleIds.isEmpty()){
            Set<Role> validRoles=new HashSet<>();
            for (Long roleId:newRoleIds){
                Role existingRole=roleRepository.findById(roleId)
                        .orElseThrow(()->new ResourceNotFoundException("Role not found with id "+roleId));
                validRoles.add(existingRole);
            }
            userData.setRoles(validRoles);
        }

        return userRepository.save(userData);
    }

    public void deleteUserById(Long id){
        User userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id "+id));
        userRepository.delete(userData) ;
    }
}
