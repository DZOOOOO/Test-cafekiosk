package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.order.OrderStatus.INIT;
import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  ProductRepository productRepository;

  @Test
  @DisplayName("결제가 완료된 주문 가져오기")
  void findOrdersBy() {
    // given
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    LocalDateTime registeredDateTime = LocalDateTime.now();

    Order order = Order.create(List.of(product1, product2, product3), registeredDateTime);
    orderRepository.save(order);

    // when
    List<Order> orders = orderRepository.findOrdersBy(
        order.getRegisteredDateTime(),
        LocalDateTime.now(),
        INIT);

    // then
    assertThat(orders).hasSize(1)
        .extracting("orderStatus", "registeredDateTime")
        .containsExactlyInAnyOrder(tuple(INIT, order.getRegisteredDateTime()));

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