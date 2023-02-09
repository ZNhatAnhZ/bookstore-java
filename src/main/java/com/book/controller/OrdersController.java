package com.book.controller;

import com.book.dto.JwtModel;
import com.book.dto.OrdersDTO;
import com.book.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/createByCart")
    public ResponseEntity<String> createOrderByCart(@RequestBody JwtModel jwtModel) {
        if (Boolean.TRUE.equals(ordersService.createOrderFromCart(jwtModel))) {
            return new ResponseEntity<>("Created order successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to create a new order", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/createById")
    public ResponseEntity<String> createOrderById(@RequestBody OrdersDTO ordersDTO) {
        if (Boolean.TRUE.equals(ordersService.createOrderFromProductId(ordersDTO))) {
            return new ResponseEntity<>("Created order successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to create a new order", HttpStatus.BAD_REQUEST);
    }
}
