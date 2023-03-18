package com.example.springsecurity.Config.Auth;

import com.example.springsecurity.Repository.UserRepository;
import com.example.springsecurity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

//시큐리티 설정에서 loginProcessingUrl("/login");
//로그인 요청이 오면 자동으로 UserDetailsService 타입으로 IOC되어 있는 loadByUSerName 함수가 실행된다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    //시큐리티 세션에 넣어주는 정보
    //authentication 객체 내부의 userdetails

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user!=null){
            return new PrincipalDetails(user);
        }
        return null;
    }
}
