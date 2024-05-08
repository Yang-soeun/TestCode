package testcode.cafekiosk.spring.docs.product;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import testcode.cafekiosk.spring.api.controller.product.ProductController;
import testcode.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import testcode.cafekiosk.spring.api.service.product.ProductService;
import testcode.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import testcode.cafekiosk.spring.api.service.product.response.ProductResponse;
import testcode.cafekiosk.spring.docs.RestDocsSupport;
import testcode.cafekiosk.spring.domain.product.ProductSellingStatus;
import testcode.cafekiosk.spring.domain.product.ProductType;

public class ProductControllerDocsTest extends RestDocsSupport {
    private final ProductService productService = Mockito.mock(ProductService.class);
    @Override
    protected Object initController() {
        return new ProductController(productService); //mock ProductService를 가지고 있는 productController
    }

    @DisplayName("신규 상품을 등록하는 API")
    @Test
    void createProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        given(productService.createProduct(any(ProductCreateServiceRequest.class)))
                .willReturn(ProductResponse.builder()
                        .id(1L)
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingStatus(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4000)
                        .build()
                );

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()), //json 형태로 보여지게 하는 것
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(STRING)
                                        .description("상품 타입"),
                                fieldWithPath("sellingStatus").type(STRING)
                                        .optional() //필수 표시
                                        .description("상품 판매상태"),
                                fieldWithPath("name").type(STRING)
                                        .description("상품 이름"),
                                fieldWithPath("price").type(NUMBER)
                                        .description("상품 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(NUMBER)
                                        .description("상품 ID"),
                                fieldWithPath("data.productNumber").type(STRING)
                                        .description("상품 번호"),
                                fieldWithPath("data.type").type(STRING)
                                        .description("상품 타입"),
                                fieldWithPath("data.sellingStatus").type(STRING)
                                        .description("상품 판매상태"),
                                fieldWithPath("data.name").type(STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.price").type(NUMBER)
                                        .description("상품 가격")
                        )
                ));
    }
}
