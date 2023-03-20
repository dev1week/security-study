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
import com.example.springsecurity.Config.oauth.Provider.*;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {



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

        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FaceBookUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("지원하지 않는 provider 입니다. ");
        }

        //로그인 진행
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId;
        System.out.println(username);
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";


        //db에 중복된 사용자가 없는지 확인
        User userEntity = userRepository.findByUsername(username);
        System.out.println(userEntity);
        if(userEntity == null){
            //없으면 db에 저장한다.
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            System.out.println("찾기2");
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
