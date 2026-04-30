package com.lianjia.service;

import com.lianjia.entity.Customer;
import com.lianjia.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    public Page<Customer> findByFilters(String name, String phone, String familyTag, 
                                         String memberLevel, Integer status, Pageable pageable) {
        return customerRepository.findByFilters(name, phone, familyTag, memberLevel, status, pageable);
    }
    
    public List<Customer> findByFiltersForExport(String name, String phone, String familyTag, 
                                                   String memberLevel, Integer status) {
        return customerRepository.findByFiltersForExport(name, phone, familyTag, memberLevel, status);
    }
    
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
    
    public List<String> findAllFamilyTags() {
        return customerRepository.findAllFamilyTags();
    }
    
    public List<String> findAllMemberLevels() {
        return customerRepository.findAllMemberLevels();
    }
}
