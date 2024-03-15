package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

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
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

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

  @Test
  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
  void findLatestProductNumber() {
    // given
    String targetProductNumber = "003";

    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);

    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    String latestProductNumber = productRepository.findLatestProductNumber();

    // then
    assertThat(latestProductNumber).isEqualTo(targetProductNumber);
  }

  @Test
  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
  void findLatestProductNumberWhenProductIsEmpty() {
    // given
    // when
    String latestProductNumber = productRepository.findLatestProductNumber();

    // then
    assertThat(latestProductNumber).isNull();
  }

  private static Product createProduct(
      String productNumber,
      ProductType type,
      ProductSellingStatus productSellingStatus,
      String productName,
      int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingStatus(productSellingStatus)
        .name(productName)
        .price(price)
        .build();
  }
}