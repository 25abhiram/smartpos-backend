package com.smartpos.backend.service;

import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    public Branch createBranch(Branch branch){
        return branchRepository.save(branch);
    }
}
