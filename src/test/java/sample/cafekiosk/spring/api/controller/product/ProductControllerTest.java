package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

class ProductControllerTest extends ControllerTestSupport {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  @DisplayName("신규 상품을 등록한다.")
  void createProduct() throws Exception {
    // given
    // REQUEST DTO
    ProductCreateRequest request = ProductCreateRequest.builder()
        .productType(HANDMADE)
        .productSellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when
    // then
    // POST API 요청
    mockMvc.perform(post("/api/v1/product/new")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다..")
  void createProductWithoutType() throws Exception {
    // given
    // REQUEST DTO
    ProductCreateRequest request = ProductCreateRequest.builder()
        .productSellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when
    // then
    // POST API 요청
    mockMvc.perform(
        post("/api/v1/product/new")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다..")
  void createProductWithoutSellingStatus() throws Exception {
    // given
    // REQUEST DTO
    ProductCreateRequest request = ProductCreateRequest.builder()
        .productType(HANDMADE)
        .name("아메리카노")
        .price(4000)
        .build();

    // when
    // then
    // POST API 요청
    mockMvc.perform(
            post("/api/v1/product/new")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다..")
  void createProductWithoutName() throws Exception {
    // given
    // REQUEST DTO
    ProductCreateRequest request = ProductCreateRequest.builder()
        .productType(HANDMADE)
        .productSellingStatus(SELLING)
        .price(4000)
        .build();

    // when
    // then
    // POST API 요청
    mockMvc.perform(
            post("/api/v1/product/new")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다..")
  void createProductWithoutZeroPrice() throws Exception {
    // given
    // REQUEST DTO
    ProductCreateRequest request = ProductCreateRequest.builder()
        .productType(HANDMADE)
        .productSellingStatus(SELLING)
        .name("아메리카노")
        .price(0)
        .build();

    // when
    // then
    // POST API 요청
    mockMvc.perform(
            post("/api/v1/product/new")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("판매 상품을 조회한다.")
  void getSellingProduct() throws Exception {
    // given
    List<ProductResponse> result = List.of();

    // 프로덕스 서비스 함수를 호출하고 결과로 빈값을 반환 받는다.
    when(productService.getSellingProducts()).thenReturn(result);

    // when
    // then
    // GET API 요청
    mockMvc.perform(
            get("/api/v1/products/selling")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
  }
}