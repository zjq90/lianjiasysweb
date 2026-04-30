package com.lianjia.repository;

import com.lianjia.entity.FamilyArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyArchiveRepository extends JpaRepository<FamilyArchive, Long> {
    Optional<FamilyArchive> findByCustomerId(Long customerId);
    boolean existsByCustomerId(Long customerId);
}
