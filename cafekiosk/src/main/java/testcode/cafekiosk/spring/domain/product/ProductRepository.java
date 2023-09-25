package testcode.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);
    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    //아이디 기준 역순 정렬 했을때 가장 상위에 있는거 가져오기 = 마지막 번호
    @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}
