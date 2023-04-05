package com.tafakkoor.e_learn.config.a;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Controller
@RequestMapping("/oauth/linkedin")
public class LinkedInController {

    private static final String REDIRECT_URI = "http://localhost:8080/oauth/linkedin/callback";

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private LinkedInConnectionFactory connectionFactory;

    @GetMapping("/connect")
    public String connect(HttpServletRequest request) {
        OAuth2Operations operations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setScope("r_liteprofile r_emailaddress");
        params.setRedirectUri(REDIRECT_URI);
        String authorizeUrl = operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);
        return "redirect:" + authorizeUrl;
    }

    @GetMapping("/callback")
    public String oauthCallback(@RequestParam("code") String code, @RequestParam("state") String state,
                                HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + code);
        HttpEntity entity = new HttpEntity("", headers);
        ResponseEntity<Map> response = restTemplate
                .exchange(URI.create("https://api.linkedin.com/v2/me"), HttpMethod.GET, entity, Map.class);
        Map userAttributes = response.getBody();
        userAttributes.entrySet().forEach(System.out::println);
        return "redirect:/profile";
    }
}
