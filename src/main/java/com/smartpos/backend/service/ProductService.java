package com.smartpos.backend.service;

import com.smartpos.backend.entity.Product;
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

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProduct(Long id,Product product){
        Product productData=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id "+id));
        productData.setName(product.getName());
        productData.setUnitPrice(product.getUnitPrice());
        productData.setStockQuantity(product.getStockQuantity());
        productData.setLowStockThreshold(product.getLowStockThreshold());
        return productRepository.save(productData);
    }

    public void updateProductStockQuantity(Product product,Double quantity){
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);
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
