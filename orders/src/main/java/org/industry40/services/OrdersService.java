package org.industry40.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.industry40.clients.CheckCustomerClient;
import org.industry40.clients.CheckInventoryClient;
import org.industry40.config.MQConfig;
import org.industry40.data.OrdersRepository;
import org.industry40.dtos.ItemDTO;
import org.industry40.dtos.OrderRequestDTO;
import org.industry40.enums.OrderStatus;
import org.industry40.exceptions.InsufficientStockException;
import org.industry40.exceptions.UnexistingOrderException;
import org.industry40.messages.OrderCancelMessage;
import org.industry40.messages.OrderCreatedMessage;
import org.industry40.models.Order;
import org.industry40.models.OrderItem;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CheckInventoryClient checkInventoryClient;

    @Autowired
    private CheckCustomerClient checkCustomerClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate template;

    public List<Order> getAllOrders() {
        return  ordersRepository.findAll();
    }

    public Order get(Integer id) throws UnexistingOrderException {
        return  ordersRepository.findById(id).orElseThrow(() -> new UnexistingOrderException("Order not found"));
    }

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {

        checkCustomerClient.validateCustomer(orderRequestDTO.getUserId());

        for (var itemRequest : orderRequestDTO.getItems()) {
            ItemDTO inventoryItem = checkInventoryClient.getStock(itemRequest.getProductId());

            if (inventoryItem.getQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock");
            }
        }

        Order order = new Order();
        order.setUserId(orderRequestDTO.getUserId());

        List<OrderItem> orderItems = orderRequestDTO.getItems().stream().map(itemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setQuantity(itemRequest.getQuantity());

            orderItem.setOrder(order);

            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = ordersRepository.save(order);

        for (var item: orderRequestDTO.getItems()) {
            OrderCreatedMessage message = new OrderCreatedMessage();
            message.setUserId(order.getUserId());
            message.setOrderId(savedOrder.getId());
            message.setProductId(item.getProductId());
            message.setQuantity(item.getQuantity());

            log.info("PASSO 1 (Orders): Enviando OrderCreatedMessage. UserId na mensagem: {}", message.getUserId());

            template.convertAndSend(
                    MQConfig.EXCHANGE,
                    MQConfig.ROUTING_KEY,
                    message
            );
        }

        return savedOrder;
    }

    public Order requestCancellation(Integer orderId) {
        Order cancelOrder =  ordersRepository.findById(orderId).orElseThrow(() -> new UnexistingOrderException("Order not found"));

        if (cancelOrder.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed order.");
        }

        cancelOrder.setStatus(OrderStatus.CANCELLING);
        ordersRepository.save(cancelOrder);

        OrderCancelMessage msg = new OrderCancelMessage(cancelOrder.getId(), cancelOrder.getUserId());
        rabbitTemplate.convertAndSend(MQConfig.ORDER_CANCEL_EXCHANGE, MQConfig.ORDER_CANCEL_ROUTING_KEY, msg);

        log.info("Cancellation requested for Order #{}", orderId);

        return cancelOrder;
    }

    public void delete(Integer Id) throws UnexistingOrderException {
        if (!ordersRepository.existsById(Id)) {
            throw new UnexistingOrderException("Product with id " + Id + " does not exist");
        }
        ordersRepository.deleteById(Id);
    }
}
