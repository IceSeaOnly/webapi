package site.binghai.lib.service.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.lib.entity.UnifiedOrder;

import java.util.List;

public interface UnifiedOrderDao extends JpaRepository<UnifiedOrder, Long> {
    List<UnifiedOrder> findAllByAppCodeOrderByCreatedDesc(Integer code, Pageable pageable);

    List<UnifiedOrder> findAllByAppCodeAndStatusOrderByCreatedDesc(Integer code, Integer status, Pageable pageable);

    Long countByAppCode(Integer code);

    Long countByAppCodeAndStatus(Integer code, Integer status);
}
