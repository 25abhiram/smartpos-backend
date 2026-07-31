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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_ShouldReturnListOfUsers(){
        User user1=new User();
        user1.setId(1L);
        User user2=new User();
        user2.setId(2L);
        List<User> mockUsers=List.of(user1,user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> actualUsers=userService.getAllUsers();

        assertEquals(2,actualUsers.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser(){
        Long userId=1L;
        User mockUser=new User();
        mockUser.setId(userId);
        mockUser.setUsername("John");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User actualUser=userService.getUserById(userId);

        assertNotNull(actualUser);
        assertEquals(userId,actualUser.getId());
        assertEquals("John",actualUser.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException(){
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception= assertThrows(
                ResourceNotFoundException.class,()->userService.getUserById(userId));

        assertEquals("User not found with id "+userId,exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateAndReturnUser(){
        Long userId=1L;
        User existingUser=new User();
        existingUser.setId(userId);

        Branch mockBranch=new Branch();
        mockBranch.setId(2L);

        Role mockRole=new Role();
        mockRole.setId(3L);

        UpdateUserRequest request=new UpdateUserRequest();
        request.setUsername("newName");
        request.setPassword("newPassword");
        request.setBranchId(2L);
        request.setRoleIds(Set.of(3L));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newName")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(branchRepository.findById(2L)).thenReturn(Optional.of(mockBranch));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(mockRole));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser=userService.updateUser(userId,request);

        assertNotNull(updatedUser);
        assertEquals(userId,updatedUser.getId());
        assertEquals("newName",updatedUser.getUsername());
        assertEquals("encodedNewPassword",updatedUser.getPassword());
        assertEquals(mockBranch,updatedUser.getBranch());
        assertTrue(updatedUser.getRoles().contains(mockRole));

        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,times(1)).existsByUsername("newName");
        verify(passwordEncoder,times(1)).encode("newPassword");
        verify(branchRepository,times(1)).findById(2L);
        verify(roleRepository,times(1)).findById(3L);
        verify(userRepository,times(1)).save(existingUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException(){
        Long userId=1L;
        UpdateUserRequest request=new UpdateUserRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->userService.updateUser(userId,request));

        assertEquals("User not found with id "+userId,exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
        verifyNoMoreInteractions(branchRepository,roleRepository,userRepository);
    }

    @Test
    void updateUser_WhenDuplicateUsername_ShouldThrowDuplicateResourceException(){
        Long userId=1L;
        User existingUser=new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldName");

        UpdateUserRequest request=new UpdateUserRequest();
        request.setUsername("takenName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("takenName")).thenReturn(true);

        DuplicateResourceException exception=assertThrows(DuplicateResourceException.class,()->userService.updateUser(userId,request));

        assertEquals("Username 'takenName' is already taken.",exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,times(1)).existsByUsername("takenName");
        verifyNoMoreInteractions(branchRepository,roleRepository);
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenBranchNotFound_ShouldThrowResourceNotFoundException(){
        Long userId=1L;
        User existingUser=new User();
        existingUser.setId(userId);

        UpdateUserRequest request=new UpdateUserRequest();
        request.setBranchId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(branchRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->userService.updateUser(userId,request));

        assertEquals("Branch not found with id 2",exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
        verify(branchRepository,times(1)).findById(2L);
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenRoleDoesNotExist_ShouldThrowException(){
        Long userId=1L;
        User existingUser=new User();
        existingUser.setId(userId);

        UpdateUserRequest request=new UpdateUserRequest();
        request.setRoleIds(Set.of(2L));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->userService.updateUser(userId,request));

        assertEquals("Role not found with id 2",exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
        verify(roleRepository,times(1)).findById(2L);
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void deleteUserById_WhenUserExists_ShouldDeleteUser(){
        Long userId=1L;
        User mockUser=new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        userService.deleteUserById(userId);

        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,times(1)).delete(mockUser);
    }

    @Test
    void deleteUserById_WhenUserDoesNotExist_ShouldThrowException(){
        Long userId=1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->userService.deleteUserById(userId));

        assertEquals("User not found with id "+userId,exception.getMessage());
        verify(userRepository,times(1)).findById(userId);
        verify(userRepository,never()).delete(any(User.class));
    }
}
