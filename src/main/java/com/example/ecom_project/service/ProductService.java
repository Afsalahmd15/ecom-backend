package com.example.ecom_project.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom_project.model.Product;
import com.example.ecom_project.repository.ProductRepo;

@Service 
public class ProductService {
    
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {

        this.productRepo = productRepo;
    }
    public List<Product> getAllProducts() {

        return productRepo.findAll();
    }

    public void addProducts(List<Product> products){

        productRepo.saveAll(products);
    }

    public Product getProductById(int id) {

        return productRepo.findById(id).orElse(null);
    }

    public Product addProduct(Product  product,MultipartFile imageFile) throws IOException{
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        
        return productRepo.save(product) ;       
    }
   
public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

    // Find the existing product
    Product existingProduct = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // Update product details
    existingProduct.setName(product.getName());
    existingProduct.setBrand(product.getBrand());
    existingProduct.setDescription(product.getDescription());
    existingProduct.setPrice(product.getPrice());
    existingProduct.setCategory(product.getCategory());
    existingProduct.setQuantity(product.getQuantity());
    existingProduct.setReleaseDate(product.getReleaseDate());
    existingProduct.setAvailable(product.isAvailable());

    // Update image only if a new image is uploaded
    if (imageFile != null && !imageFile.isEmpty()) {
        existingProduct.setImageData(imageFile.getBytes());
        existingProduct.setImageName(imageFile.getOriginalFilename());
        existingProduct.setImageType(imageFile.getContentType());
    }

    return productRepo.save(existingProduct);
}


    public void deleteProduct(int id) {

        productRepo.deleteById(id);
    }

    public List<Product> searchProduct(String name) {

        return productRepo.searchProduct(name);
    }
}
