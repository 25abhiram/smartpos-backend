package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateProductRequest;
import com.smartpos.backend.dto.UpdateProductRequest;
import com.smartpos.backend.entity.Product;
import com.smartpos.backend.exceptions.DuplicateResourceException;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        List<Product> products=productRepository.findAll();
        return products;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
    }

    public Product createProduct(CreateProductRequest productRequest){
        if (productRepository.existsByName(productRequest.getName())){
            throw new DuplicateResourceException("Product with name '"+productRequest.getName()+"' already exists");
        }

        Product product=new Product();
        product.setName(productRequest.getName());
        product.setUnitPrice(productRequest.getUnitPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setLowStockThreshold(productRequest.getLowStockThreshold());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, UpdateProductRequest productRequest){
        Product productData=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));

        String newProductName=productRequest.getName();
        if (newProductName!=null && !newProductName.trim().isEmpty() && !newProductName.equals(productData.getName())){
            if (productRepository.existsByNameAndIdNot(newProductName,productData.getId())){
                throw new DuplicateResourceException("Product with name '"+newProductName+"' already exists");
            }
            productData.setName(newProductName);
        }

        if (productRequest.getUnitPrice()!=null){
            productData.setUnitPrice(productRequest.getUnitPrice());
        }

        if (productRequest.getStockQuantity()!=null){
            productData.setStockQuantity(productRequest.getStockQuantity());
        }

        if (productRequest.getLowStockThreshold()!=null){
            productData.setLowStockThreshold(productRequest.getLowStockThreshold());
        }

        return productRepository.save(productData);
    }

    public void deleteProductById(Long id){
        Product productData=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
        productRepository.delete(productData);
    }

    public List<String> getLowStockAlertsForAdmin(){
        List<Product> lowStockProducts=productRepository.findLowStockProducts();

        if (lowStockProducts.isEmpty()){
            return List.of("All product stock levels are healthy.");
        }

        return lowStockProducts.stream().map(product -> "WARNING: Product '"+product.getName()+"' is low on stock! Current stock: "+product.getStockQuantity()+", Threshold stock level: "+product.getLowStockThreshold()).toList();
    }
}
