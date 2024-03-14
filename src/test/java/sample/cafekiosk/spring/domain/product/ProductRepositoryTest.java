package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test") // application.yml 파일에서 프로파일별 설정 정보를 적용한다.
// @SpringBootTest // spring 서버를 띄워서 테스트를 가능하게 만들어준다.
@DataJpaTest // JPA Bean 만 띄움. @SpringBootTest 보다는 가볍다. -> 빠른 테스트가 가능, 데이터를 롤백 시킨다.
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
  void findAllBySellingStatusIn() {
    // given
    Product product1 = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingStatus(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();

    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingStatus(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();

    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    List<Product> products = productRepository.findAllBySellingStatusIn(
        List.of(SELLING, HOLD));

    // then
    assertThat(products).hasSize(2)
        // 필드 추출
        .extracting("productNumber", "name", "sellingStatus")
        // 순서에 맞게 정확하게 필드 값들이 맞는지 체크
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );

  }

  @Test
  @DisplayName("상품번호 리스트로 상품들을 조회한다.")
  void findAllByProductNumberIn() {
    // given
    Product product1 = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingStatus(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();

    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingStatus(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();

    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    List<Product> products = productRepository.findAllByProductNumberIn(
        List.of("001", "002"));

    // then
    assertThat(products).hasSize(2)
        // 필드 추출
        .extracting("productNumber", "name", "sellingStatus")
        // 순서에 맞게 정확하게 필드 값들이 맞는지 체크
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );

  }
}