package com.estore.demo.common.security;

import com.estore.demo.common.domain.CustomUser;
import com.estore.demo.common.domain.UserInfo;
import com.estore.demo.user.repo.IUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads user name from entries managed by DaoUserManager
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private IUserInfoRepository userInfoRepository;

    /*
    finds user name from database and create a custom user with principal and authorities
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!"".equals(username)) {

            CustomUser user = null;
            UserInfo userInfo = userInfoRepository.findByUsername(username);

            List authorities = new ArrayList();
            if (null != userInfo) {
                String userRoles = userInfo.getRoles();

                if (null != userRoles) {
                    String[] userRolesArray = userRoles.split(",");

                    for (String userRole : userRolesArray) {
                        authorities.add(new SimpleGrantedAuthority(userRole));
                    }

                    String encodedPassword = passwordEncoder.encode(userInfo.getPassword());

                    user = new CustomUser(username,
                            encodedPassword, true, true, true, true,
                            authorities, userInfo.getId());

                }
            } else {
                throw new UsernameNotFoundException(username);
            }
            return user;
        }
        return null;
    }


}