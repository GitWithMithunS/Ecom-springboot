package com.ecommerce.ecom_proj.controller;

import com.ecommerce.ecom_proj.model.Product;
import com.ecommerce.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:5175")  // ✅ allow React frontend
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String hello(){
        return "hello their";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProduct() , HttpStatus.OK);
        // return new ResponseEntity<>(service.getAllProduct() );  //this is wrong (a http status code must be sent along with it
        // return ResponseEntity.ok(service.getAllProduct());  //this is correct
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable  int prodId){
        Product product = service.getProductByID(prodId);

        if(product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK );
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product ,
                                        @RequestPart MultipartFile imageFile){
        try {
            Product product1 = service.addproduct(product , imageFile);
            return new ResponseEntity<>(product1 , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR );
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product");  //alternate way to write this
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageProductId(@PathVariable int prodId){
        Product product = service.getProductByID(prodId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

}
