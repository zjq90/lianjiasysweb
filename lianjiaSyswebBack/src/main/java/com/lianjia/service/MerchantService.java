package com.lianjia.service;

import com.lianjia.dto.LoginRequest;
import com.lianjia.dto.LoginResponse;
import com.lianjia.entity.Merchant;
import com.lianjia.repository.MerchantRepository;
import com.lianjia.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MerchantService {
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String token = jwtTokenUtil.generateToken(request.getUsername());
        Merchant merchant = merchantRepository.findByUsername(request.getUsername()).orElse(null);
        
        return LoginResponse.builder()
                .token(token)
                .username(merchant.getUsername())
                .merchantName(merchant.getMerchantName())
                .phone(merchant.getPhone())
                .build();
    }
    
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }
    
    public Optional<Merchant> findById(Long id) {
        return merchantRepository.findById(id);
    }
    
    public Optional<Merchant> findByUsername(String username) {
        return merchantRepository.findByUsername(username);
    }
    
    public Merchant save(Merchant merchant) {
        if (merchant.getId() == null) {
            merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        } else {
            Merchant existing = merchantRepository.findById(merchant.getId()).orElse(null);
            if (existing != null && !merchant.getPassword().equals(existing.getPassword())) {
                merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
            }
        }
        return merchantRepository.save(merchant);
    }
    
    public void deleteById(Long id) {
        merchantRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return merchantRepository.existsByUsername(username);
    }
}
