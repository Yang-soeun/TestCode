package testcode.cafekiosk.spring.api.service.product.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import testcode.cafekiosk.spring.domain.product.Product;
import testcode.cafekiosk.spring.domain.product.ProductSellingStatus;
import testcode.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class ProductCreateServiceRequest {

    private ProductType type;
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private ProductCreateServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

    /**
     * validation 책임 분리에 대해서...
     * String name에 대해서 상품 이름은 20자 제한
     * @Max(20)을 사용하여 문자열을 체크할 수 있지만 이걸 여기서 검증하는게 맞는지에 대해서 생각 해봐야 한다.
     * String에 대한 최소한의 조건은 controller 단에서 검증하고
     * 20자 제한은 Service layer, domain 생성 시점에서 검증을 하든 더 안쪽 Layer에서 검증을 하는것이 더 좋다.
     */
}
