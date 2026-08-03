package com.smartpos.backend.service;

import com.smartpos.backend.dto.CreateProductRequest;
import com.smartpos.backend.dto.UpdateProductRequest;
import com.smartpos.backend.entity.Product;
import com.smartpos.backend.exceptions.DuplicateResourceException;
import com.smartpos.backend.exceptions.ResourceNotFoundException;
import com.smartpos.backend.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp(){
        testProduct=new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setUnitPrice(100.0);
        testProduct.setStockQuantity(50.0);
        testProduct.setLowStockThreshold(10.0);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts(){
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> actualProducts=productService.getAllProducts();

        assertEquals(1,actualProducts.size());
        verify(productRepository,times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product actualProduct=productService.getProductById(1L);

        assertNotNull(actualProduct);
        assertEquals(1L,actualProduct.getId());
        assertEquals("Test Product",actualProduct.getName());
        assertEquals(100.0,actualProduct.getUnitPrice());
        assertEquals(50.0,actualProduct.getStockQuantity());
        assertEquals(10.0,actualProduct.getLowStockThreshold());

        verify(productRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->productService.getProductById(1L));

        assertEquals("Product not found with id 1",exception.getMessage());
        verify(productRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void createProduct_WhenNameIsUnique_ShouldSaveProduct(){
        CreateProductRequest request=new CreateProductRequest();
        request.setName("New Product");
        request.setUnitPrice(50.0);
        request.setStockQuantity(20.0);
        request.setLowStockThreshold(5.0);

        Product newProduct=new Product();
        newProduct.setId(2L);
        newProduct.setName("New Product");
        newProduct.setUnitPrice(50.0);
        newProduct.setStockQuantity(20.0);
        newProduct.setLowStockThreshold(5.0);

        when(productRepository.existsByName("New Product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        Product actualProduct=productService.createProduct(request);

        assertNotNull(actualProduct);
        assertEquals(2L,actualProduct.getId());
        assertEquals("New Product",actualProduct.getName());
        assertEquals(50.0,actualProduct.getUnitPrice());
        assertEquals(20.0,actualProduct.getStockQuantity());
        assertEquals(5.0,actualProduct.getLowStockThreshold());

        verify(productRepository,times(1)).existsByName("New Product");
        verify(productRepository,times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void createProduct_WhenDuplicateProductName_ShouldThrowDuplicateResourceException(){
        CreateProductRequest request=new CreateProductRequest();
        request.setName("Existing Product");

        when(productRepository.existsByName("Existing Product")).thenReturn(true);

        DuplicateResourceException exception=assertThrows(DuplicateResourceException.class,()->productService.createProduct(request));

        assertEquals("Product with name 'Existing Product' already exists",exception.getMessage());
        verify(productRepository,times(1)).existsByName("Existing Product");
        verify(productRepository,never()).save(any(Product.class));
    }

    @Test
    void updateProduct_WithValidData_ShouldUpdateAndReturnProduct(){
        Long productId=2L;
        Product existingProduct=new Product();
        existingProduct.setId(productId);

        UpdateProductRequest request=new UpdateProductRequest();
        request.setName("Updated Name");
        request.setUnitPrice(50.0);
        request.setStockQuantity(20.0);
        request.setLowStockThreshold(5.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByNameAndIdNot("Updated Name",2L)).thenReturn(false);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product updatedProduct=productService.updateProduct(productId,request);

        assertNotNull(updatedProduct);
        assertEquals(2L,updatedProduct.getId());
        assertEquals("Updated Name",updatedProduct.getName());
        assertEquals(50.0,updatedProduct.getUnitPrice());
        assertEquals(20.0,updatedProduct.getStockQuantity());
        assertEquals(5.0,updatedProduct.getLowStockThreshold());

        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,times(1)).existsByNameAndIdNot("Updated Name",productId);
        verify(productRepository,times(1)).save(existingProduct);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException(){
        UpdateProductRequest request=new UpdateProductRequest();
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->productService.updateProduct(2L,request));

        assertEquals("Product not found with id 2",exception.getMessage());
        verify(productRepository,times(1)).findById(2L);
        verify(productRepository,never()).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct_WhenDuplicateProductName_ShouldThrowDuplicateResourceException(){
        Long productId=2L;
        Product existingProduct=new Product();
        existingProduct.setId(productId);

        UpdateProductRequest request=new UpdateProductRequest();
        request.setName("Updated Name");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByNameAndIdNot("Updated Name",2L)).thenReturn(true);

        DuplicateResourceException exception=assertThrows(DuplicateResourceException.class,()->productService.updateProduct(productId,request));

        assertEquals("Product with name 'Updated Name' already exists",exception.getMessage());
        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,times(1)).existsByNameAndIdNot("Updated Name",productId);
        verifyNoMoreInteractions(productRepository);
        verify(productRepository,never()).save(any(Product.class));
    }

    @Test
    void deleteProductById_WhenProductExists_ShouldDeleteProduct(){
        Long productId=2L;
        Product existingProduct=new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).delete(existingProduct);

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void deleteProductById_WhenProductDoesNotExist_ShouldThrowException(){
        Long productId=2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->productService.deleteProductById(productId));

        assertEquals("Product not found with id 2",exception.getMessage());
        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,never()).delete(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getLowStockAlertsForAdmin_WhenStockIsLow_ShouldReturnAlerts(){
        when(productRepository.findLowStockProducts()).thenReturn(List.of(testProduct));

        List<String> alerts=productService.getLowStockAlertsForAdmin();

        assertEquals(1,alerts.size());
        assertTrue(alerts.get(0).contains("WARNING: Product '"+testProduct.getName()+"' is low on stock! Current stock: "+testProduct.getStockQuantity()+", Threshold stock level: "+testProduct.getLowStockThreshold()));
        verify(productRepository,times(1)).findLowStockProducts();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getLowStockAlertsForAdmin_WhenNoLowStock_ShouldReturnHealthyMessage(){
        when(productRepository.findLowStockProducts()).thenReturn(List.of());

        List<String> alerts=productService.getLowStockAlertsForAdmin();

        assertEquals(1,alerts.size());
        assertEquals("All product stock levels are healthy.",alerts.get(0));
        verify(productRepository,times(1)).findLowStockProducts();
    }
}
