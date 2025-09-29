package com.ecommerce.ecom_proj.service;

import com.ecommerce.ecom_proj.model.Product;
import com.ecommerce.ecom_proj.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public Product getProductByID(int prodId) {
        return productRepository.findById(prodId).orElse(null);
    }

    public Product addproduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return productRepository.save(product);
    }


    public Product updateProduct(int prodId, Product product, MultipartFile imageFile) throws IOException {
        if(!productRepository.existsById(prodId)){
            throw new RuntimeException("product of id" + prodId + " not found");
        }

        product.setId(prodId);   //important

        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());

        productRepository.save(product);

        return product;
    }

    public void deleteProduct(int prodId) {
        productRepository.deleteById(prodId);
    }


    public List<Product> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword);
    }
}
