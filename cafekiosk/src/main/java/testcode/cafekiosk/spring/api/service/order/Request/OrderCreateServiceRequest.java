package testcode.cafekiosk.spring.api.service.order.Request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumbers;//상품번호 리스트

    @Builder
    private OrderCreateServiceRequest(List<String> productNumbers) {
        this.productNumbers =productNumbers;
    }
}
