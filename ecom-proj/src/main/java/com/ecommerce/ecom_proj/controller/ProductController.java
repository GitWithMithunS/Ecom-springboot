package com.ecommerce.ecom_proj.controller;

import com.ecommerce.ecom_proj.model.Product;
import com.ecommerce.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
        return "hello their . this page is not having anything ";
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

    @PostMapping(value = "/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try {
            Product saved = service.addproduct(product, imageFile);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageProductId(@PathVariable int prodId) {
        Product product = service.getProductByID(prodId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{prodId}")
    public ResponseEntity<String> updateProduct(@PathVariable int prodId ,
                                                @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile) throws IOException {
        Product product1 = null;

        try {
            product1 = service.updateProduct(prodId , product , imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to Update" , HttpStatus.BAD_REQUEST);
        }

        if(product1 == null){
            return new ResponseEntity<>("Failed to update" , HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("Updated successfully" , HttpStatus.OK);
        }
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId){
        Product product = service.getProductByID(prodId);
        if(product == null){
            return new ResponseEntity<>("Product not found" , HttpStatus.NOT_FOUND);
        }

        service.deleteProduct(prodId);
        return new ResponseEntity<>("Product deleted successfully" ,  HttpStatus.OK );
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        System.out.println("Searching with " + keyword);
        List<Product> products = service.searchProduct(keyword);

        return new ResponseEntity<>(products , HttpStatus.OK);
    }
}





//    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> addProduct(@RequestPart("product") String productJson,
//                                        @RequestPart("imageFile") MultipartFile imageFile) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Product product = mapper.readValue(productJson, Product.class);
//
//            Product saved = service.addproduct(product, imageFile);
//            return new ResponseEntity<>(saved, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
