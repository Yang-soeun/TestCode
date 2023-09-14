package testcode.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    private List<String> productNumbers;//상품번호 리스트

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers =productNumbers;
    }
}
