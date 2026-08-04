package com.smartpos.backend.controller;

import com.smartpos.backend.dto.CreateBranchRequest;
import com.smartpos.backend.dto.UpdateBranchRequest;
import com.smartpos.backend.entity.Branch;
import com.smartpos.backend.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping
    public ResponseEntity<Branch> createBranch(@Valid @RequestBody CreateBranchRequest request){
        Branch createdBranch=branchService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

    @GetMapping("/allbranches")
    public ResponseEntity<List<Branch>> getAllBranches(){
        List<Branch> branches=branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id){
        Branch branch=branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id,@RequestBody UpdateBranchRequest request){
        Branch updatedBranch=branchService.updateBranch(id,request);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchById(@PathVariable Long id){
        branchService.deleteBranchById(id);
        return ResponseEntity.noContent().build();
    }

}
