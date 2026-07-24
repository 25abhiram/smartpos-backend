package com.smartpos.backend.service;

import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    public Branch createBranch(Branch branch){
        return branchRepository.save(branch);
    }

    public List<Branch> getAllBranches(){
        List<Branch> branches=branchRepository.findAll();
        return branches;
    }

    public Branch getBranchById(Long id){
        return branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));
    }

    public Branch updateBranch(Long id,Branch branch){
        Branch branchData=branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));
        branchData.setName(branch.getName());
        branchData.setAddress(branch.getAddress());
        return branchRepository.save(branchData);
    }

    public void deleteBranchById(Long id){
        Branch branchData=branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));
        branchRepository.delete(branchData);
    }

}
