package testcode.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import testcode.cafekiosk.spring.api.ApiResponse;
import testcode.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import testcode.cafekiosk.spring.api.service.order.OrderService;
import testcode.cafekiosk.spring.api.service.order.response.OrderResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request){
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDateTime));
    }
    /**
     * OrderCreateRequest 그냥 넘겨주게 되면 하위 레이어가 상위 레이어를 알게 된다.
     * controller layer가 service layer를 주입 받아서 사용하는 형태이므로 상위는 하위를 호출하므로 알고 있는게 당연하지만
     * 하위가 상위를 알고 있는건 좋은 형태가 아니다.
     * 그래서 service 용 dto를 따로 만들어서 변환해서 넘겨준다.
     * 서비스 layer에서는 controller에 대한 정보가 없고 컨트롤러에서도 controller 용 dto 만 쓰기 때문에 분리가 된다.
     * 더 좋은건 service 모듈을 분리하면 bean validation 부분이 필요가 없어진다.
     */
}
