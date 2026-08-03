package com.smartpos.backend.controller;

import com.smartpos.backend.dto.CreateProductRequest;
import com.smartpos.backend.dto.UpdateProductRequest;
import com.smartpos.backend.entity.Product;
import com.smartpos.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/allproducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products=productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody CreateProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,@Valid @RequestBody UpdateProductRequest productRequest){
        return productService.updateProduct(id,productRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }


}
