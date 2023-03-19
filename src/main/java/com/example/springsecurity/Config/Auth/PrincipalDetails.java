package com.example.springsecurity.Config.Auth;
import com.example.springsecurity.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//시큐리티가 / login 주소요청이 오면 낚아서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티가 가지고 있는 session을 만들어준다. (Security ContextHolder라는 키를 갖는 세션을 찾으면 됨)
// 오브젝트 => Authentication 타입 객체만 세션에 들어갈 수 있음
//Authentication 안에 User 정보가 있어야한다.
//User 오브젝트 타입 => UserDetails 타입 객체


//시큐리티가 가지고 있는 세션 안에 세션정보를 넣을려면 Authentication 객체여야한다. => Authentication 안에 user를 저장할려면 UserDetails로 넣어줘야한다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user; //콤포지션

    private Map<String, Object> attributes;

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        //return attributes.get("sub");
        return null;
    }

    //일반 로그인 생성자
    public PrincipalDetails(User user){
        this.user = user;
    }

    //oauth 로그인 생성자
    public PrincipalDetails (User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    //해당 유저의 권한정보를 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole());

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
