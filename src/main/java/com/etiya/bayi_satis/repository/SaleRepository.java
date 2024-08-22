package com.etiya.bayi_satis.repository;

import com.etiya.bayi_satis.entity.Sale;
import com.etiya.bayi_satis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUser(User user);
    Optional<Sale> findById(Long id);
}
