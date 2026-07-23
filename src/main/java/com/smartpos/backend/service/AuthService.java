package com.smartpos.backend.service;

import com.smartpos.backend.dto.JwtResponse;
import com.smartpos.backend.dto.SignupRequest;
import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.entity.ERole;
import com.smartpos.backend.entity.Role;
import com.smartpos.backend.entity.User;
import com.smartpos.backend.repository.BranchRepository;
import com.smartpos.backend.repository.RoleRepository;
import com.smartpos.backend.repository.UserRepository;
import com.smartpos.backend.security.JwtUtil;
import com.smartpos.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ProductService productService;

    @Autowired
    JwtUtil jwtUtils;

    public JwtResponse authenticateUser(User user){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        String jwt=jwtUtils.generateToken(userDetails);

        List<String> roles=userDetails.getAuthorities().stream()
                .map(role->role.getAuthority()).toList();

        List<String> lowStockAlerts=null;
        boolean isAdmin=roles.stream().anyMatch(role->role.equals("ROLE_ADMIN"));

        if (isAdmin){
            lowStockAlerts=productService.getLowStockAlertsForAdmin();
        }

        return new JwtResponse(jwt, userDetails.getUsername(),roles,lowStockAlerts);
    }

    public String registerUser(SignupRequest signupRequest){
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            return "Error: Username is already taken!";
        }
        User newUser=new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(encoder.encode(signupRequest.getPassword()));

        Branch branch=branchRepository.findById(signupRequest.getBranchId())
                .orElseThrow(()->new RuntimeException("Branch not found"));
        newUser.setBranch(branch);

        Set<Role> roles=new HashSet<>();
        String requestRole= signupRequest.getRole();

        if (requestRole!=null && requestRole.equalsIgnoreCase("admin")){
            Role adminRole=roleRepository.findByName(ERole.ADMIN)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found in database."));
            roles.add(adminRole);
        }else {
            Role cashierRole=roleRepository.findByName(ERole.CASHIER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found in database."));
            roles.add(cashierRole);
        }

        newUser.setRoles(roles);

        userRepository.save(newUser);
        return "User registered successfully!";
    }
}
