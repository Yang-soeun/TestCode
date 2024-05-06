package testcode.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import testcode.cafekiosk.spring.api.controller.order.OrderController;
import testcode.cafekiosk.spring.api.controller.product.ProductController;
import testcode.cafekiosk.spring.api.service.order.OrderService;
import testcode.cafekiosk.spring.api.service.product.ProductService;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class
})
public class ControllerTestSupport {

    @Autowired//컨테이너에서 주입
    protected MockMvc mockMvc;//서비스 레이어 하위로는 mocking처리를 하기 위해서 사용하는거

    @Autowired
    protected ObjectMapper objectMapper;//직렬화(JSON), 역직렬화를 하기 위해서

    @MockBean
    protected OrderService orderService;

    @MockBean//컨테이너에 mock(ProductService mock 객체) 객체를 넣어주는 역할
    protected ProductService productService;
}
