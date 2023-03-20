package com.example.springsecurity.Config.oauth.Provider;

import java.util.Map;

public class FaceBookUserInfo implements OAuth2UserInfo{
    //Oauth2User.getAttributes()를 메모리에 저장
    private Map<String, Object> attributes;

    public FaceBookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }
}
