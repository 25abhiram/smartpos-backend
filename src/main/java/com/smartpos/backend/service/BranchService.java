package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateBranchRequest;
import com.smartpos.backend.dto.UpdateBranchRequest;
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

    public Branch createBranch(CreateBranchRequest request){
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setAddress(request.getAddress());
        return branchRepository.save(branch);
    }

    public List<Branch> getAllBranches(){
        List<Branch> branches=branchRepository.findAll();
        return branches;
    }

    public Branch getBranchById(Long id){
        return branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));
    }

    public Branch updateBranch(Long id, UpdateBranchRequest request){
        Branch branchData=branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));

        String name= request.getName();
        if (name!=null && !name.trim().isEmpty()){
            branchData.setName(name);
        }

        String address=request.getAddress();
        if (address!=null && !address.trim().isEmpty()){
            branchData.setAddress(address);
        }

        return branchRepository.save(branchData);
    }

    public void deleteBranchById(Long id){
        Branch branchData=branchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Branch not found with id "+id));
        branchRepository.delete(branchData);
    }

}
