package com.aniekanudoessien.controller;

import com.aniekanudoessien.model.PriceChange;
import com.aniekanudoessien.model.responseproduct.ProductInfo;
import com.aniekanudoessien.service.ProductService;
import com.aniekanudoessien.util.MyRetailPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(MyRetailPath.MYRETAIL_BASE_PATH)
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItemInfo(@PathVariable Long productId) throws Exception{

        ProductInfo productInfo = productService.getById(productId);

        return new ResponseEntity<>(productInfo, HttpStatus.OK);
    }

    @PostMapping(value = "/prices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> setPriceData(@RequestBody PriceChange priceChange) throws Exception {

        ProductInfo productInfo = productService.setPrice(priceChange);

        return new ResponseEntity<>(productInfo, HttpStatus.CREATED);
    }

    @PutMapping(value = "/products/{productId}")
    public ResponseEntity<Object> updatePrice(@PathVariable Long productId,
                                              @RequestBody ProductInfo productInfo) throws Exception{

        ProductInfo updatedProduct = productService.updatePrice(productId, productInfo);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long productId) throws Exception{

        String deletedResponse = productService.deleteProduct(productId);

        return new ResponseEntity<>(deletedResponse, HttpStatus.OK);
    }
}
