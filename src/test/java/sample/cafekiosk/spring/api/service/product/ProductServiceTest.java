package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

class ProductServiceTest extends IntegrationTestSupport {

  @Autowired
  ProductService productService;

  @Autowired
  ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    productRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
  void createProduct() {
    // given
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    productRepository.save(product1);

    ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
        .productType(HANDMADE)
        .productSellingStatus(SELLING)
        .name("카푸치노")
        .price(5000)
        .build();

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertThat(productResponse)
        .extracting("ProductNumber", "type", "sellingStatus", "name", "price")
        .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(2)
        .extracting("ProductNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
            tuple("002", HANDMADE, SELLING, "카푸치노", 5000));
  }

  @Test
  @DisplayName("상품이 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
  void createProductWhenProductIsEmpty() {
    // given
    ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
        .productType(HANDMADE)
        .productSellingStatus(SELLING)
        .name("카푸치노")
        .price(5000)
        .build();

    // when
    ProductResponse productResponse = productService.createProduct(request);

    // then
    assertThat(productResponse)
        .extracting("ProductNumber", "type", "sellingStatus", "name", "price")
        .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(1)
        .extracting("ProductNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
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