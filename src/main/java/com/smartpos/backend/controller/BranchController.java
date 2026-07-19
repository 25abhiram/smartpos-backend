package com.smartpos.backend.controller;

import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch){
        Branch createdBranch=branchService.createBranch(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

}
