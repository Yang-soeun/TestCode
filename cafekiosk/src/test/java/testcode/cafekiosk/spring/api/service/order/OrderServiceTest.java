package testcode.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import testcode.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import testcode.cafekiosk.spring.api.service.order.response.OrderResponse;
import testcode.cafekiosk.spring.domain.order.OrderRepository;
import testcode.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import testcode.cafekiosk.spring.domain.product.Product;
import testcode.cafekiosk.spring.domain.product.ProductRepository;
import testcode.cafekiosk.spring.domain.product.ProductType;
import testcode.cafekiosk.spring.domain.stock.Stock;
import testcode.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static testcode.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static testcode.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
//@Transactional
@SpringBootTest//통합테스트
//@DataJpaTest//이게 스프링부트테스트 보다 가벼움 근데 스프링부트 테스트를 권장함
class OrderServiceTest {
    @Autowired private OrderService orderService;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private StockRepository stockRepository;

    /**
     * 서비스에 transactional을 하면 rollback이 되기 때문에 해주지 않아도 된다.
     */

    @AfterEach//테스트가 끝날때마다 delete 해줌
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock /...
     */
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        //given - 테스트를 위한 준비단계
        /**
         * 이렇게 하면 코드가 너무 지저분하다
         */
//        Product product1 = Product.builder()
//                .productNum("001")
//                .type(HANDMADE)
//                .sellingStatus(SELLING)
//                .name("아메리카노")
//                .price(4000)
//                .build();
//
//        Product product2 = Product.builder()
//                .productNum("002")
//                .type(HANDMADE)
//                .sellingStatus(HOLD)
//                .name("카페라떼")
//                .price(4500)
//                .build();
//
//        Product product3 = Product.builder()
//                .productNum("003")
//                .type(HANDMADE)
//                .sellingStatus(STOP_SELLING)
//                .name("팥빙수")
//                .price(7000)
//                .build();

//        productRepository.saveAll(List.of(product1, product2, product3));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2= createProduct(HANDMADE, "002", 3000);
        Product product3= createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();


        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2= createProduct(HANDMADE, "002", 3000);
        Product product3= createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))// 똑같은거 두개 주문하기
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2= createProduct(BAKERY, "002", 3000);
        Product product3= createProduct(HANDMADE, "003", 5000);

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1, stock2));

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001","002", "003"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 10000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );

        //재고가 잘 감소했는지
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );

    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2= createProduct(BAKERY, "002", 3000);
        Product product3= createProduct(HANDMADE, "003", 5000);

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stock1.deductQuantity(1); //todo -> 이렇게 작성하면 안된다!!!
        stockRepository.saveAll(List.of(stock1, stock2));

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001","002", "003"))
                .build();

        //when //then

        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");

    }



    //필요한것만 입력받고 나머지는 그냥 기본값 쓰도록 만들어서 사용하기
    private Product createProduct(ProductType type, String productNum, int price){
        return Product.builder()
                .type(type)
                .productNum(productNum)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}