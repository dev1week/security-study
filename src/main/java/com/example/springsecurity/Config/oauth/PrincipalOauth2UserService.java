package com.example.springsecurity.Config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //oauth 로그인 후처리 메서드는 loadUser 메서드 이다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: "+userRequest.getClientRegistration()); //registrationId로 어디서 로그인했는지 확인가능
        System.out.println("userRequest: "+userRequest.getAccessToken());
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (Oauth 클라이언트 라이브러리)->액세스 토큰 요청
        // 유저 리퀘스트 정보 -> 회원 프로필 받아야함 (loadUser 함수) -> 구글로부터 회원 프로필 받아옴
        System.out.println("userRequest: "+super.loadUser(userRequest).getAttributes());

        OAuth2User oauth2User = super.loadUser(userRequest);


        return super.loadUser(userRequest);
    }
}
