package com.example.springsecurity.Controller;

import com.example.springsecurity.Config.Auth.PrincipalDetails;
import com.example.springsecurity.Repository.UserRepository;
import com.example.springsecurity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                            @AuthenticationPrincipal UserDetails userDetails){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: "+principalDetails.getUser());
        //authentication: User(id=2, username=admin, password=$2a$10$pMxuiTsE5HXoW/fQRvgtC.kMOTYXfvNZc4IOfrL..TPFjofBtA1bC, email=admin@naver.com, role=ROLE_ADMIN, provider=null, providerId=null, createDate=2023-03-18 23:27:13.467)
        //authentication: User(id=2, username=admin, password=$2a$10$pMxuiTsE5HXoW/fQRvgtC.kMOTYXfvNZc4IOfrL..TPFjofBtA1bC, email=admin@naver.com, role=ROLE_ADMIN, provider=null, providerId=null, createDate=2023-03-18 23:27:13.467)

        System.out.println("userDetilas: "+userDetails.getUsername());
        //serDetilas: admin
        return "세션정보 확인됨";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
                                                @AuthenticationPrincipal OAuth2User oauth){
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: "+oauth2User.getAttributes());
        //authentication: {sub=116704309515134533612, name=김한주, given_name=한주, family_name=김, picture=https://lh3.googleusercontent.com/a/AGNmyxZaeAvGD2eM1R3WKKpl10wqqOY_zRFu-UneiwYLP-0=s96-c, email=rondo2860@gmail.com, email_verified=true, locale=ko}
        System.out.println("oauth2user : "+oauth.getAttributes());
        //oauth2user : {sub=116704309515134533612, name=김한주, given_name=한주, family_name=김, picture=https://lh3.googleusercontent.com/a/AGNmyxZaeAvGD2eM1R3WKKpl10wqqOY_zRFu-UneiwYLP-0=s96-c, email=rondo2860@gmail.com, email_verified=true, locale=ko}
        return "세션정보 확인됨";
    }

    @GetMapping({"", "/"})
    public String index(){
        return"index";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails userDetails){

        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }


    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }


    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/";
    }
//
//    //@Secured("ADMIN")
//    @PreAuthorize("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')") // 함수 시작전
//    @PostAuthorize(...                                               // 함수 종료 후
//    @GetMapping("/info")
//    public @ResponseBody String info(){
//        return "개인정보";
//    }

}
