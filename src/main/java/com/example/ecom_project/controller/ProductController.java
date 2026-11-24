package com.example.ecom_project.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom_project.model.Product;
import com.example.ecom_project.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){

        this.productService = productService;
    }


    @RequestMapping("/")
    public String greet() {

        return "homeeeee";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){

        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }
    
    @PostMapping("/addProducts")
    public String addProducts(
        @RequestBody List<Product> products
    ){

        productService.addProducts(products);

        return "added Successfully";
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(
        @PathVariable int id
    ){
        Product product = productService.getProductById(id);
        if(product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
        }
            
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @PostMapping(value = "/addProduct",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
        @RequestPart Product product,
        @RequestPart MultipartFile imageFile
    ) {

        
        try{

            Product product1 = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }catch(Exception e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   @GetMapping("/product/{id}/image")
   public ResponseEntity<byte[]> getImageByProductId(
    @PathVariable int id
   ) {

        Product product = productService.getProductById(id);

        byte[] imageFile = product.getImageData();

        return  ResponseEntity.ok()
        .contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
   }
   
   @PutMapping(value = "/product/{id}",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

   public ResponseEntity<String> updateProduct(

    @PathVariable int id,
        @RequestPart("product") Product product,
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile)
   {

    Product product1 = null;
    try {

        product1 =productService.updateProduct(id, product, imageFile);
    }catch(IOException e) {

        return new ResponseEntity<>("failed to update", HttpStatus.BAD_REQUEST);
    }
    
   
    if(product1 != null) {

        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

        return new ResponseEntity<>("failed to update", HttpStatus.BAD_REQUEST);
   }

   @DeleteMapping("/product/{id}")
   public ResponseEntity<String> deleteProduct(

        @PathVariable int id
   ){
        Product product = productService.getProductById(id);
        if(product != null) {

            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Product not Found", HttpStatus.NOT_FOUND);
   }
   @GetMapping("/product/search")
   public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        System.out.println("Searching with keyword " + name);
    return new ResponseEntity<>(productService.searchProduct(name), HttpStatus.OK);
   }
    
}
