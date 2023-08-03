package testcode.cafekiosk.unit;

import lombok.Getter;
import testcode.cafekiosk.unit.beverage.Beverage;
import testcode.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//요구사항을 충족 시키는 객체
@Getter
public class CafeKiosk {

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    //한개 삭제
    public void remove(Beverage beverage){
        beverages.remove(beverage);
    }

    //전체 삭제
    public void clear(){
        beverages.clear();
    }
    public int calculateTotalPrice() {
        int totalPrice = 0;

        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }

        return totalPrice;
    }

    public Order createOrder(){//주문 생성
        return new Order(LocalDateTime.now(), beverages);
    }
}
