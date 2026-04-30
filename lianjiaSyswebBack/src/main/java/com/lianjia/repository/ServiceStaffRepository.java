package com.lianjia.repository;

import com.lianjia.entity.ServiceStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceStaffRepository extends JpaRepository<ServiceStaff, Long> {
    
    @Query("SELECT s FROM ServiceStaff s WHERE " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:skill IS NULL OR s.skill LIKE %:skill%)")
    List<ServiceStaff> findByFilters(
            @Param("name") String name,
            @Param("status") String status,
            @Param("skill") String skill);
    
    List<ServiceStaff> findByStatus(String status);
}
