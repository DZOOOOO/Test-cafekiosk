package sample.cafekiosk.spring.api.service.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateServiceRequest {

  private ProductType productType;
  private ProductSellingStatus productSellingStatus;
  private String name;
  private int price;

  @Builder
  private ProductCreateServiceRequest(
      ProductType productType,
      ProductSellingStatus productSellingStatus,
      String name, int price) {
    this.productType = productType;
    this.productSellingStatus = productSellingStatus;
    this.name = name;
    this.price = price;
  }

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .productNumber(nextProductNumber)
        .type(productType)
        .sellingStatus(productSellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}
