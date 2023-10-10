package testcode.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import testcode.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import testcode.cafekiosk.spring.api.service.product.ProductService;
import testcode.cafekiosk.spring.api.service.product.response.ProductResponse;
import testcode.cafekiosk.spring.domain.product.ProductSellingStatus;
import testcode.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Presentation Layer 테스트
 * 외부 세계의 요청을 가장 먼저 받는 계층
 * 파라미터에 대한 최소한의 검증을 수행한다
 */
@WebMvcTest(controllers = ProductController.class)//컨트롤러 레이어만 테스트 하기 위해서, 테스트 하고자 하는 클래스 명시해주면 OK
class ProductControllerTest {

    @Autowired//컨테이너에서 주입
    private MockMvc mockMvc;//서비스 레이어 하위로는 mocking처리를 하기 위해서 사용하는거

    @Autowired
    private ObjectMapper objectMapper;//직렬화(JSON), 역직렬화를 하기 위해서서

    @MockBean//컨테이너에 mock(ProductService mock 객체) 객체를 넣어주는 역할
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다")
    @Test
    void createProduct() throws Exception {//성공 테스트
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        //when //then
        //api를 쏘는 수행
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("신규 상품을 등록할때 상품 타입은 필수입니다.")
    @Test
    void createProductWithoutType() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }


    @DisplayName("신규 상품을 등록할때 상품 판매상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("신규 상품을 등록할때 상품 이름은 필수입니다.")
    @Test
    void createProductWithoutName() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .build();

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("신규 상품을 등록할때 상품 가격은 양수이다.")
    @Test
    void createProductWithZeroPrice() throws Exception { //경계값 테스트
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception { //경계값 테스트
        //given
        List<ProductResponse> result = List.of();

        //빈배열로 해도 되는 이유
        //값들이 오는것을 확인해도 되지만 그런것들은 이미 service layer 에서 다 테스트를 했기 때문에 하지 않아도 된다.
        //그래서 형태만 잘 오는지 확인하면 된다
        when(productService.getSellingProducts()).thenReturn(result);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")
                )
                .andDo(MockMvcResultHandlers.print())//자세한 로그를 볼 수 있음
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
        ;
    }
}