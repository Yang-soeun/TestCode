package testcode.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import testcode.cafekiosk.spring.api.ApiResponse;
import testcode.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import testcode.cafekiosk.spring.api.service.product.ProductService;
import testcode.cafekiosk.spring.api.service.product.response.ProductResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> crateProduct(@RequestBody @Valid ProductCreateRequest request){
        return ApiResponse.ok(productService.createProduct(request.toServiceRequest()));
    }
    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts(){
        return ApiResponse.ok(productService.getSellingProducts());
    }
}
