package com.lianjia.repository;

import com.lianjia.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAll(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:phone IS NULL OR c.phone LIKE %:phone%) AND " +
           "(:familyTag IS NULL OR c.familyTag = :familyTag) AND " +
           "(:memberLevel IS NULL OR c.memberLevel = :memberLevel) AND " +
           "(:status IS NULL OR c.status = :status)")
    Page<Customer> findByFilters(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("familyTag") String familyTag,
            @Param("memberLevel") String memberLevel,
            @Param("status") Integer status,
            Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:phone IS NULL OR c.phone LIKE %:phone%) AND " +
           "(:familyTag IS NULL OR c.familyTag = :familyTag) AND " +
           "(:memberLevel IS NULL OR c.memberLevel = :memberLevel) AND " +
           "(:status IS NULL OR c.status = :status)")
    List<Customer> findByFiltersForExport(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("familyTag") String familyTag,
            @Param("memberLevel") String memberLevel,
            @Param("status") Integer status);
    
    @Query("SELECT DISTINCT c.familyTag FROM Customer c WHERE c.familyTag IS NOT NULL")
    List<String> findAllFamilyTags();
    
    @Query("SELECT DISTINCT c.memberLevel FROM Customer c WHERE c.memberLevel IS NOT NULL")
    List<String> findAllMemberLevels();
}
