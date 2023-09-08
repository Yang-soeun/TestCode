package testcode.cafekiosk.spring.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import testcode.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;

    private String productNum;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    @Builder
    private Product(String name, int price, String productNum, ProductType type, ProductSellingStatus sellingStatus) {
        this.name = name;
        this.price = price;
        this.productNum = productNum;
        this.type = type;
        this.sellingStatus = sellingStatus;
    }
}
