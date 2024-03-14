package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StockRepositoryTest {

  @Autowired
  private StockRepository stockRepository;

  @Test
  @DisplayName("상품번호 리스트로 재고를 조회한다.")
  void findAllByProductNumberIn() {
    // given
    Stock stock = Stock.create("001", 1);
    Stock stock2 = Stock.create("002", 2);
    Stock stock3 = Stock.create("003", 3);
    stockRepository.saveAll(List.of(stock, stock2, stock3));

    // when
    List<Stock> stocks = stockRepository.findAllByProductNumberIn(
        List.of("001", "002", "003"));

    // then
    assertThat(stocks)
        .hasSize(3)
        .extracting("productNumber", "quantity")
        .containsExactlyInAnyOrder(
            tuple("001", 1),
            tuple("002", 2),
            tuple("003", 3)
        );

  }
}