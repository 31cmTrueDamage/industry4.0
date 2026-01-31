package org.industry40.controllers;

import jakarta.validation.Valid;
import org.industry40.dtos.OrderDTO;
import org.industry40.dtos.OrderRequestDTO;
import org.industry40.models.Order;
import org.industry40.services.OrdersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    private ModelMapper modelMapper;

    public  OrdersController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping
    ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> products = ordersService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(products.stream().map(c -> modelMapper.map(c, OrderDTO.class))
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        Order newOrder = ordersService.createOrder(orderRequestDTO);
        OrderDTO responseDTO = modelMapper.map(newOrder, OrderDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Integer id) {
        Order updatedOrder = ordersService.requestCancellation(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(updatedOrder, OrderDTO.class));
    }
}
