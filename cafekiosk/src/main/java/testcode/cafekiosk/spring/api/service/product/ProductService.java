package testcode.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testcode.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import testcode.cafekiosk.spring.api.service.product.response.ProductResponse;
import testcode.cafekiosk.spring.domain.product.Product;
import testcode.cafekiosk.spring.domain.product.ProductRepository;
import testcode.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        //productNumber
        //001 002 003 004
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

//        return ProductResponse.builder()
//                .id(savedProduct.getId())
//                .productNumber(savedProduct.getProductNumber())
//                .type(savedProduct.getType())
//                .sellingStatus(savedProduct.getSellingStatus())
//                .name(savedProduct.getName())
//                .price(savedProduct.getPrice())
//                .build();

        return ProductResponse.of(savedProduct);
    }
    public List<ProductResponse> getSellingProducts(){
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }

    private String createNextProductNumber(){
        //DB에서 마지막 저장된 프로덕트의 상품 번호를 읽어와서
        String latestProductNumber = productRepository.findLatestProductNumber();
        //+1
        if(latestProductNumber == null)
            return "001";

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt +1;

        //9 -> 009 10-> 010
        return String.format("%03d", nextProductNumberInt);
    }

}
