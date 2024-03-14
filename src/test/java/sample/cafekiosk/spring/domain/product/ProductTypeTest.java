package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

  @Test
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  void containsStockType() {
    // given
    ProductType productType = ProductType.HANDMADE;

    // when
    boolean result = ProductType.containsStockType(productType);

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  void containsStockType2() {
    // given
    ProductType productType = ProductType.BAKERY;

    // when
    boolean result = ProductType.containsStockType(productType);

    // then
    assertThat(result).isTrue();
  }
}