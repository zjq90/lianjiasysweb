package com.lianjia.service;

import com.lianjia.entity.ConsumptionRecord;
import com.lianjia.repository.ConsumptionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConsumptionRecordService {
    
    @Autowired
    private ConsumptionRecordRepository consumptionRecordRepository;
    
    public Page<ConsumptionRecord> findByCustomerId(Long customerId, Pageable pageable) {
        return consumptionRecordRepository.findByCustomerId(customerId, pageable);
    }
    
    public List<ConsumptionRecord> findByCustomerIdOrderByConsumptionTimeDesc(Long customerId) {
        return consumptionRecordRepository.findByCustomerIdOrderByConsumptionTimeDesc(customerId);
    }
    
    public Page<ConsumptionRecord> findByFilters(Long customerId, String serviceType, 
                                                  String status, Pageable pageable) {
        return consumptionRecordRepository.findByFilters(customerId, serviceType, status, pageable);
    }
    
    public Optional<ConsumptionRecord> findById(Long id) {
        return consumptionRecordRepository.findById(id);
    }
    
    public ConsumptionRecord save(ConsumptionRecord consumptionRecord) {
        return consumptionRecordRepository.save(consumptionRecord);
    }
    
    public void deleteById(Long id) {
        consumptionRecordRepository.deleteById(id);
    }
}
