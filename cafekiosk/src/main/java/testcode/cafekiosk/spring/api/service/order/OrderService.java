package testcode.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testcode.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import testcode.cafekiosk.spring.api.service.order.Request.OrderCreateServiceRequest;
import testcode.cafekiosk.spring.api.service.order.response.OrderResponse;
import testcode.cafekiosk.spring.domain.order.Order;
import testcode.cafekiosk.spring.domain.order.OrderRepository;
import testcode.cafekiosk.spring.domain.product.Product;
import testcode.cafekiosk.spring.domain.product.ProductRepository;
import testcode.cafekiosk.spring.domain.product.ProductType;
import testcode.cafekiosk.spring.domain.stock.Stock;
import testcode.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        //Product
        List<Product> products = findProductsBy(productNumbers);

        deductStockQuantities(products);
//        Order order = Order.create(products, registeredDateTime);
        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(List<Product> products) {
        //재고 차감 체크가 필요한 상품들 filter -> 재고와 관련된 상품 번호들만 골라내기
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        //재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        //상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        //재고 차감 시도
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)){//예외상황: 재고보다 주문한 양이 많은 경우
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        return productCountingMap;
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

//        Map<String, Stock> stockMap = stocks.stream()
//                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
//        return stockMap;

        //간단하게 crtl + alt + N
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        return stockProductNumbers;
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);//IN 절로 하면 001, 001등 중복되는 상품을 넣은 경우 중복이 제거된다

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        List<Product> duplicateProducts = productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
        return duplicateProducts;
    }
}
