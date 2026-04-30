package com.lianjia.repository;

import com.lianjia.entity.ConsumptionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, Long> {
    Page<ConsumptionRecord> findByCustomerId(Long customerId, Pageable pageable);
    
    @Query("SELECT c FROM ConsumptionRecord c WHERE " +
           "(:customerId IS NULL OR c.customerId = :customerId) AND " +
           "(:serviceType IS NULL OR c.serviceType = :serviceType) AND " +
           "(:status IS NULL OR c.status = :status)")
    Page<ConsumptionRecord> findByFilters(
            @Param("customerId") Long customerId,
            @Param("serviceType") String serviceType,
            @Param("status") String status,
            Pageable pageable);
    
    List<ConsumptionRecord> findByCustomerIdOrderByConsumptionTimeDesc(Long customerId);
}
