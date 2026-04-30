package com.lianjia.security;

import com.lianjia.entity.Merchant;
import com.lianjia.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Merchant merchant = merchantRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("商户不存在: " + username));
        
        if (merchant.getStatus() != 1) {
            throw new RuntimeException("商户已被禁用");
        }
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MERCHANT"));
        
        return new User(
                merchant.getUsername(),
                merchant.getPassword(),
                authorities
        );
    }
}
