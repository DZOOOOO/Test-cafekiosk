package sample.cafekiosk.spring.api.controller.product.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

  @NotNull(message = "상품 타입은 필수입니다.")
  private ProductType productType;

  @NotNull(message = "상품 판매상태는 필수입니다.")
  private ProductSellingStatus productSellingStatus;

  @NotBlank(message = "상품 이름은 필수입니다.")
  private String name;

  @Positive(message = "상품 가격은 양수여야 합니다.")
  private int price;

  @Builder
  private ProductCreateRequest(ProductType productType, ProductSellingStatus productSellingStatus,
      String name, int price) {
    this.productType = productType;
    this.productSellingStatus = productSellingStatus;
    this.name = name;
    this.price = price;
  }

  public ProductCreateServiceRequest toServiceRequest() {
    return ProductCreateServiceRequest.builder()
        .productType(productType)
        .productSellingStatus(productSellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}
