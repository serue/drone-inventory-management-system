package com.nyasha.drone.backend.order;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final HttpSession session;
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order getOrder(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void save(OrderRequest request) {
        String orderReference = UUID.randomUUID().toString();
        Order order =  Order.builder()
                .category(request.getCategory())
                .orderReference(orderReference)
                .delivered(false)
                .build();
        Order savedOrder = orderRepository.save(order);
        session.setAttribute("order", savedOrder);
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public Order update(int id) {
        Order order = getOrder(id);
        order.setDelivered(true);
        return orderRepository.save(order);
    }
}
