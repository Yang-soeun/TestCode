package testcode.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import testcode.cafekiosk.unit.beverage.Americano;
import testcode.cafekiosk.unit.beverage.Latte;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {
    @Test
    void add_manual_test(){//수동 테스트
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수: " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료: " + cafeKiosk.getBeverages().get(0).getName());
    }

    @Test
    void add(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
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
}