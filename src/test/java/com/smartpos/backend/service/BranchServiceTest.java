package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateBranchRequest;
import com.smartpos.backend.dto.UpdateBranchRequest;
import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.BranchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {
    @Mock
    BranchRepository branchRepository;

    @InjectMocks
    BranchService branchService;

    @Test
    void createBranch_whenValidRequestFields_shouldSaveBranch(){
        CreateBranchRequest request=new CreateBranchRequest();
        request.setName("New Branch Name");
        request.setAddress("New Branch Address");

        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation ->{
            Branch newBranch=invocation.getArgument(0);
            newBranch.setId(2L);
            return newBranch;
        });

        Branch result=branchService.createBranch(request);

        assertNotNull(request);
        assertEquals(2L,result.getId());
        assertEquals("New Branch Name", result.getName());
        assertEquals("New Branch Address", result.getAddress());

        verify(branchRepository,times(1)).save(any(Branch.class));
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void getAllBranches_shouldReturnListOfBranches(){
        Branch branch1=new Branch();
        branch1.setId(1L);
        Branch branch2=new Branch();
        branch2.setId(2L);
        List<Branch> mockBranches=List.of(branch1,branch2);

        when(branchRepository.findAll()).thenReturn(mockBranches);

        List<Branch> result=branchService.getAllBranches();

        assertEquals(2,result.size());
        verify(branchRepository,times(1)).findAll();
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void getBranchById_whenBranchExists_shouldReturnBranch(){
        Long branchId=1L;
        Branch existingBranch=new Branch();
        existingBranch.setId(branchId);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(existingBranch));

        Branch result=branchService.getBranchById(branchId);

        assertNotNull(result);
        assertEquals(branchId,result.getId());
        verify(branchRepository,times(1)).findById(branchId);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void getBranchById_WhenBranchDoesNotExist_ShouldThrowException(){
        Long branchId=1L;
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->branchService.getBranchById(branchId));

        assertEquals("Branch not found with id "+branchId,exception.getMessage());
        verify(branchRepository,times(1)).findById(branchId);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void updateBranch_WithValidData_ShouldUpdateAndReturnBranch(){
        Long branchId=1L;
        UpdateBranchRequest request=new UpdateBranchRequest();
        request.setName("Updated Branch Name");
        request.setAddress("Updated Branch Address");

        Branch existingBranch=new Branch();
        existingBranch.setId(branchId);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(existingBranch));
        when(branchRepository.save(existingBranch)).thenReturn(existingBranch);

        Branch result=branchService.updateBranch(branchId,request);

        assertNotNull(result);
        assertEquals(branchId,result.getId());
        assertEquals("Updated Branch Name", result.getName());
        assertEquals("Updated Branch Address", result.getAddress());
        verify(branchRepository,times(1)).findById(branchId);
        verify(branchRepository,times(1)).save(existingBranch);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void updateBranch_WhenBranchDoesNotExist_ShouldThrowException(){
        Long branchId=1L;
        UpdateBranchRequest request=new UpdateBranchRequest();
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->branchService.updateBranch(branchId,request));

        assertEquals("Branch not found with id "+branchId,exception.getMessage());
        verify(branchRepository,times(1)).findById(branchId);
        verify(branchRepository,never()).save(any(Branch.class));
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void deleteBranchById_WhenBranchExists_ShouldDeleteBranch(){
        Long branchId=1L;
        Branch existingBranch=new Branch();
        existingBranch.setId(branchId);

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(existingBranch));
        doNothing().when(branchRepository).delete(existingBranch);

        branchService.deleteBranchById(branchId);

        verify(branchRepository,times(1)).findById(branchId);
        verify(branchRepository,times(1)).delete(existingBranch);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void deleteBranchById_WhenBranchDoesNotExist_ShouldThrowException(){
        Long branchId=1L;
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->branchService.deleteBranchById(branchId));

        assertEquals("Branch not found with id "+branchId,exception.getMessage());
        verify(branchRepository,times(1)).findById(branchId);
        verify(branchRepository,never()).delete(any(Branch.class));
        verifyNoMoreInteractions(branchRepository);
    }
}
