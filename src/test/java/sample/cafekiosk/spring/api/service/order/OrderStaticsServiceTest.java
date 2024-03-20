package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

class OrderStaticsServiceTest extends IntegrationTestSupport {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Autowired
  OrderStaticsService orderStaticsService;

  @Autowired
  MailSendHistoryRepository mailSendHistoryRepository;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
  void sendOrderStatisticsMail() {
    // given
    LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
    Order order2 = createPaymentCompletedOrder(now, products);
    Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
    Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

    // Mock 객체 행위 기대값.. Stubbing
    Mockito.when(mailSendClient.sendEmail(
        any(String.class),
        any(String.class),
        any(String.class),
        any(String.class))).thenReturn(true);

    // when
    boolean result = orderStaticsService.sendOrderStatistMail(LocalDate.of(2023, 3, 5),
        "test@test.com");

    // then
    assertThat(result).isTrue();

    List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
    assertThat(histories).hasSize(1)
        .extracting("content")
        .contains("총 매출 합계는 31000원입니다.");

  }

  private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
    Order order = Order.builder()
        .products(products)
        .orderStatus(OrderStatus.PAYMENT_COMPLETED)
        .registeredDateTime(now)
        .build();
    return orderRepository.save(order);
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