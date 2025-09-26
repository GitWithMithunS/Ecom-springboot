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
}
