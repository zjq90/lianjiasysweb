package com.lianjia.repository;

import com.lianjia.entity.ServiceOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    
    @Query("SELECT o FROM ServiceOrder o WHERE " +
           "(:orderNo IS NULL OR o.orderNo LIKE %:orderNo%) AND " +
           "(:customerName IS NULL OR EXISTS (SELECT c FROM Customer c WHERE c.id = o.customerId AND c.name LIKE %:customerName%)) AND " +
           "(:staffId IS NULL OR o.staffId = :staffId) AND " +
           "(:serviceType IS NULL OR o.serviceType = :serviceType) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<ServiceOrder> findByFilters(
            @Param("orderNo") String orderNo,
            @Param("customerName") String customerName,
            @Param("staffId") Long staffId,
            @Param("serviceType") String serviceType,
            @Param("status") String status,
            Pageable pageable);
    
    List<ServiceOrder> findByCustomerIdOrderByCreateTimeDesc(Long customerId);
    
    List<ServiceOrder> findByStaffIdOrderByCreateTimeDesc(Long staffId);
    
    List<ServiceOrder> findByStatusOrderByCreateTimeDesc(String status);
}
