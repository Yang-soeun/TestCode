package testcode.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static testcode.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static testcode.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")//test profile로 돌리기
@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn(){
        //given
        Product product1 = Product.builder()
                .productNum("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNum("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNum("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));
        //when

        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        /**
         * 리스트에 대한 검증은 이런식으로 하기!!
         * 1. size 체크
         * 2. 검증하고자 하는 필드만 추출하여 검증(extraction - contains~ 구조)
         */
        assertThat(products).hasSize(2)
                .extracting("productNum", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
}