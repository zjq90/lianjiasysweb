package com.lianjia.service;

import com.lianjia.entity.FamilyArchive;
import com.lianjia.repository.FamilyArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FamilyArchiveService {
    
    @Autowired
    private FamilyArchiveRepository familyArchiveRepository;
    
    public List<FamilyArchive> findAll() {
        return familyArchiveRepository.findAll();
    }
    
    public Optional<FamilyArchive> findById(Long id) {
        return familyArchiveRepository.findById(id);
    }
    
    public Optional<FamilyArchive> findByCustomerId(Long customerId) {
        return familyArchiveRepository.findByCustomerId(customerId);
    }
    
    public FamilyArchive save(FamilyArchive familyArchive) {
        return familyArchiveRepository.save(familyArchive);
    }
    
    public void deleteById(Long id) {
        familyArchiveRepository.deleteById(id);
    }
    
    public boolean existsByCustomerId(Long customerId) {
        return familyArchiveRepository.existsByCustomerId(customerId);
    }
}
