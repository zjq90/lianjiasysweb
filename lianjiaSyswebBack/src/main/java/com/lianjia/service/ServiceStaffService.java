package com.lianjia.service;

import com.lianjia.entity.ServiceStaff;
import com.lianjia.repository.ServiceStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceStaffService {
    
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;
    
    public List<ServiceStaff> findAll() {
        return serviceStaffRepository.findAll();
    }
    
    public List<ServiceStaff> findByFilters(String name, String status, String skill) {
        return serviceStaffRepository.findByFilters(name, status, skill);
    }
    
    public List<ServiceStaff> findByStatus(String status) {
        return serviceStaffRepository.findByStatus(status);
    }
    
    public Optional<ServiceStaff> findById(Long id) {
        return serviceStaffRepository.findById(id);
    }
    
    public ServiceStaff save(ServiceStaff serviceStaff) {
        return serviceStaffRepository.save(serviceStaff);
    }
    
    public void deleteById(Long id) {
        serviceStaffRepository.deleteById(id);
    }
    
    public ServiceStaff updateStatus(Long id, String status) {
        Optional<ServiceStaff> existing = serviceStaffRepository.findById(id);
        if (existing.isPresent()) {
            ServiceStaff staff = existing.get();
            staff.setStatus(status);
            return serviceStaffRepository.save(staff);
        }
        return null;
    }
}
