package testcode.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import testcode.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import testcode.cafekiosk.spring.api.service.order.response.OrderResponse;
import testcode.cafekiosk.spring.domain.order.Order;
import testcode.cafekiosk.spring.domain.order.OrderRepository;
import testcode.cafekiosk.spring.domain.product.Product;
import testcode.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        //Product
        List<Product> products = productRepository.findAllByProductNumIn(productNumbers);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }
}
