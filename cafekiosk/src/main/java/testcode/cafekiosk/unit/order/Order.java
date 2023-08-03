package testcode.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import testcode.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {
    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
