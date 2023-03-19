package com.example.springsecurity.Config.oauth;

import com.example.springsecurity.Config.Auth.PrincipalDetails;
import com.example.springsecurity.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.example.springsecurity.domain.User;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //oauth 로그인 후처리 메서드는 loadUser 메서드 이다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //System.out.println("userRequest: "+userRequest.getClientRegistration()); //registrationId로 어디서 로그인했는지 확인가능
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (Oauth 클라이언트 라이브러리)->액세스 토큰 요청
        // 유저 리퀘스트 정보 -> 회원 프로필 받아야함 (loadUser 함수) -> 구글로부터 회원 프로필 받아옴


        //구글에서 로그인 정보 받아오기
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("userRequest: "+oauth2User.getAttributes());

        //로그인 진행
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oauth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("더미");
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USEER";


        //db에 중복된 사용자가 없는지 확인
        User userEntity = userRepository.findByUsername(username);

        if(userEntity != null){
            //없으면 db에 저장한다.
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
