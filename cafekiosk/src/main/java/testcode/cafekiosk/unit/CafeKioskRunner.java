package testcode.cafekiosk.unit;

import testcode.cafekiosk.unit.beverage.Americano;
import testcode.cafekiosk.unit.beverage.Latte;
import testcode.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

public class CafeKioskRunner {
    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();//객체 생성
        cafeKiosk.add(new Americano());
        System.out.println(">>> 아메리카노 추가");

        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문 가격 : " + totalPrice);

        Order order = cafeKiosk.createOrder(LocalDateTime.now());//실제로는 이렇게 사용하고 테스트에서는 직접 원하는 시간을 넣어서 테스트
    }
}
