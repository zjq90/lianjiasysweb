package com.lianjia.service;

import com.lianjia.entity.Customer;
import com.lianjia.entity.ServiceOrder;
import com.lianjia.entity.ServiceStaff;
import com.lianjia.repository.CustomerRepository;
import com.lianjia.repository.ServiceOrderRepository;
import com.lianjia.repository.ServiceStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceOrderService {
    
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;
    
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Page<ServiceOrder> findByFilters(String orderNo, String customerName, Long staffId,
                                             String serviceType, String status, Pageable pageable) {
        Page<ServiceOrder> orders = serviceOrderRepository.findByFilters(
                orderNo, customerName, staffId, serviceType, status, pageable);
        
        orders.getContent().forEach(this::populateOrderDetails);
        return orders;
    }
    
    public Optional<ServiceOrder> findById(Long id) {
        Optional<ServiceOrder> order = serviceOrderRepository.findById(id);
        order.ifPresent(this::populateOrderDetails);
        return order;
    }
    
    public List<ServiceOrder> findByCustomerId(Long customerId) {
        List<ServiceOrder> orders = serviceOrderRepository.findByCustomerIdOrderByCreateTimeDesc(customerId);
        orders.forEach(this::populateOrderDetails);
        return orders;
    }
    
    public List<ServiceOrder> findByStaffId(Long staffId) {
        List<ServiceOrder> orders = serviceOrderRepository.findByStaffIdOrderByCreateTimeDesc(staffId);
        orders.forEach(this::populateOrderDetails);
        return orders;
    }
    
    public List<ServiceOrder> findByStatus(String status) {
        List<ServiceOrder> orders = serviceOrderRepository.findByStatusOrderByCreateTimeDesc(status);
        orders.forEach(this::populateOrderDetails);
        return orders;
    }
    
    public ServiceOrder save(ServiceOrder serviceOrder) {
        if (serviceOrder.getOrderNo() == null) {
            serviceOrder.setOrderNo("SO" + System.currentTimeMillis());
        }
        return serviceOrderRepository.save(serviceOrder);
    }
    
    public ServiceOrder assignStaff(Long orderId, Long staffId) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return null;
        }
        
        Optional<ServiceStaff> staffOpt = serviceStaffRepository.findById(staffId);
        if (!staffOpt.isPresent()) {
            return null;
        }
        
        ServiceStaff staff = staffOpt.get();
        if (!"空闲".equals(staff.getStatus())) {
            throw new RuntimeException("服务人员当前状态为" + staff.getStatus() + "，无法派单");
        }
        
        ServiceOrder order = orderOpt.get();
        order.setStaffId(staffId);
        order.setStatus("待服务");
        
        staff.setStatus("忙碌");
        serviceStaffRepository.save(staff);
        
        return serviceOrderRepository.save(order);
    }
    
    public ServiceOrder startService(Long orderId) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return null;
        }
        
        ServiceOrder order = orderOpt.get();
        if (!"待服务".equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态为" + order.getStatus() + "，无法开始服务");
        }
        
        order.setStatus("服务中");
        order.setActualStartTime(LocalDateTime.now());
        order.setProgress("正在进行服务...");
        
        return serviceOrderRepository.save(order);
    }
    
    public ServiceOrder completeService(Long orderId, String progress, String feedback, Double rating) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return null;
        }
        
        ServiceOrder order = orderOpt.get();
        if (!"服务中".equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态为" + order.getStatus() + "，无法完成服务");
        }
        
        order.setStatus("已完成");
        order.setActualEndTime(LocalDateTime.now());
        order.setProgress(progress);
        order.setCustomerFeedback(feedback);
        order.setRating(rating);
        
        if (order.getStaffId() != null) {
            Optional<ServiceStaff> staffOpt = serviceStaffRepository.findById(order.getStaffId());
            if (staffOpt.isPresent()) {
                ServiceStaff staff = staffOpt.get();
                staff.setStatus("空闲");
                staff.setOrderCount(staff.getOrderCount() + 1);
                
                if (rating != null) {
                    if (staff.getRating() == null) {
                        staff.setRating(rating);
                    } else {
                        double newRating = (staff.getRating() * staff.getOrderCount() + rating) / (staff.getOrderCount() + 1);
                        staff.setRating(Math.round(newRating * 10.0) / 10.0);
                    }
                }
                serviceStaffRepository.save(staff);
            }
        }
        
        return serviceOrderRepository.save(order);
    }
    
    public ServiceOrder cancelOrder(Long orderId, String remark) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return null;
        }
        
        ServiceOrder order = orderOpt.get();
        if ("已完成".equals(order.getStatus()) || "已取消".equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态为" + order.getStatus() + "，无法取消");
        }
        
        order.setStatus("已取消");
        if (remark != null) {
            order.setRemark(remark);
        }
        
        if (order.getStaffId() != null) {
            Optional<ServiceStaff> staffOpt = serviceStaffRepository.findById(order.getStaffId());
            if (staffOpt.isPresent()) {
                ServiceStaff staff = staffOpt.get();
                staff.setStatus("空闲");
                serviceStaffRepository.save(staff);
            }
        }
        
        return serviceOrderRepository.save(order);
    }
    
    public void deleteById(Long id) {
        serviceOrderRepository.deleteById(id);
    }
    
    private void populateOrderDetails(ServiceOrder order) {
        if (order.getCustomerId() != null) {
            Optional<Customer> customerOpt = customerRepository.findById(order.getCustomerId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                order.setCustomerName(customer.getName());
                order.setCustomerPhone(customer.getPhone());
                order.setCustomerAddress(customer.getAddress());
            }
        }
        
        if (order.getStaffId() != null) {
            Optional<ServiceStaff> staffOpt = serviceStaffRepository.findById(order.getStaffId());
            if (staffOpt.isPresent()) {
                ServiceStaff staff = staffOpt.get();
                order.setStaffName(staff.getName());
                order.setStaffPhone(staff.getPhone());
            }
        }
    }
}
