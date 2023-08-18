package testcode.cafekiosk.unit;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testcode.cafekiosk.unit.beverage.Americano;
import testcode.cafekiosk.unit.beverage.Latte;
import testcode.cafekiosk.unit.order.Order;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {
    @Test
    void add_manual_test(){//수동 테스트
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수: " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료: " + cafeKiosk.getBeverages().get(0).getName());
    }

    // displayname을 자세하게!!
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")//JUnit5부터 생긴 이름
    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    /**
     * 테스트 케이스 세분화하기
     */
    @Test//해피 케이스 테스트
    void addSeveralBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test//예외 케이스 테스트
    void addZeroBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //예외 테스트 방법
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);//1개 추가
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);

        cafeKiosk.remove(americano);//1개 제거
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
    }

    @Test
    void clear(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);//1개 추가
        cafeKiosk.add(latte);//1개 추가
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();//전체 제거
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    /**
     * TDD
     * Test Code 작성
     */
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when -> 보통 한줄인 경우가 많다
        int totalPrice = cafeKiosk.calculateTotalPrice();

        //then -> 검증 단계
        assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    void createOrder(){//이렇게 작성하면 운영시간 안에서만 성공하는 테스트 케이스가 되어버린다
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

    }

    @Test
    void createOrderWithCurrentTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 8, 4, 10, 0));//경계값 테스트
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

    }

    @Test
    void createOrderOutsideOpenTime(){//예외 테스트
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThatThrownBy(()-> cafeKiosk.createOrder(LocalDateTime.of(2023, 8, 4, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");

    }

}