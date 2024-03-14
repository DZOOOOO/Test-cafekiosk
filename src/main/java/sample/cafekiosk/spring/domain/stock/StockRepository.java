package sample.cafekiosk.spring.domain.stock;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

  /**
   * select *
   * from stock
   * where product_number in(원하는 상품번호);
   */
  List<Stock> findAllByProductNumberIn(List<String> productNumbers);

}
