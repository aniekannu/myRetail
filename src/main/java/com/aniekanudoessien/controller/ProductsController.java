package com.aniekanudoessien.controller;

import com.aniekanudoessien.model.PriceChange;
import com.aniekanudoessien.model.responseproduct.ProductInfo;
import com.aniekanudoessien.service.ProductService;
import com.aniekanudoessien.util.MyRetailPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(MyRetailPath.MYRETAIL_BASE_PATH)
public class ProductsController {

    @Autowired
    private ProductService productService;

    // for admin
    // json object data from our client request body is bound to the PriceChange object
    // PriceChange class consists of the productId and (price) value fields
    @PostMapping(value = MyRetailPath.RESOURCE_PRICES)
    public ResponseEntity<Object> setPriceData(@RequestBody PriceChange priceChange) throws Exception {

        ProductInfo productInfo = productService.setPrice(priceChange);

        return new ResponseEntity<>(productInfo, HttpStatus.CREATED);
    }

    @DeleteMapping(value = MyRetailPath.RESOURCE_PATH_VARIABLE)
    public ResponseEntity<Object> deleteItem(@PathVariable Long productId) throws Exception{

        String deletedResponse = productService.deleteProduct(productId);

        return new ResponseEntity<>(deletedResponse, HttpStatus.OK);
    }






    // for client
    @GetMapping(value = MyRetailPath.RESOURCE_PATH_VARIABLE)
    public ResponseEntity<Object> getItemInfo(@PathVariable Long productId) throws Exception{

        ProductInfo productInfo = productService.getById(productId);

        return new ResponseEntity<>(productInfo, HttpStatus.OK);
    }

    @PutMapping(value = MyRetailPath.RESOURCE_PATH_VARIABLE)
    public ResponseEntity<Object> updatePrice(@PathVariable Long productId,
                                              @RequestBody ProductInfo productInfo) throws Exception{

        ProductInfo updatedProduct = productService.updatePrice(productId, productInfo);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
