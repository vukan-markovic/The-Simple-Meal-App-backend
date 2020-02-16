package com.example.Fooddeliverysystem.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.example.Fooddeliverysystem.model.User;
import com.example.Fooddeliverysystem.repository.UserRepo;
import com.example.Fooddeliverysystem.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null || user.getStatus() == Status.INACTIVE.getValue()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
		/*List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (UserAuthority ua: user.getUserAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(ua.getAuthority().getName()));
		}*/

            //Java 1.8 way
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    grantedAuthorities);
        }
    }

    @Autowired
    public void setUserRepository(UserRepo userRepository) {
        this.userRepository = userRepository;
    }
}