# myRetail
Spring Boot REST API that presents the pricing information of qualified products in MyRetail

## Overview
myRetail is a rapidly growing company with HQ in Richmond, VA and over 200 stores across the east coast. myRetail wants to make its internal data available to any number of client devices, from myRetail.com to native mobile apps.

## Tech Stack
- Java String Boot
- MongoDB

## App Design
The application is setup to validate every product id that is provided to its endpoints against Redsky Product Catalog. Afterwards, it checks a specified mongoDB database for a product id match before any one of the 4 CRUD operations are performed. Ultimately, a response entity is returned to the calling client, along with a HTTP status response code that indicates the outcome of the operation. 

## Notable Class Objects
- Redsky Catalog response body is bound to the CatalogProduct class
- MongoDB document field values are bound to the Price class
- Client reponse body is bound to the ProductInfo class

## How to interact with myRetailAPI
- Postman
- Insomnia
- cURL

## Endpoints
1. **GET** request @ http://myretail/v1/products/{productId}/
2. **POST** request @ http://myretail/v1/products/prices/
3. **PUT** request @ http://myretail/v1/products/{productId}/
4. **DELETE** request @ http://myretail/v1/products/{productId}/

## Sample Product IDs
- 51514132
- 16953856
- 52177069
- 79369123
- 16700918
- 16476125
- 14641685

## Sample Response JSON Data
```
{
    "id": 51514132,
    "name": "2 Year Headphones &#38; Speakers Protection Plan with Accidents coverage",
    "current_price": {
        "value": 35.49,
        "currency_code": "USD"
    }
}
```

## Testing
- 7 Integration tests

