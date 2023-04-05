package com.tafakkoor.e_learn.controller;

import com.tafakkoor.e_learn.repository.AuthUserRepository;
import com.tafakkoor.e_learn.services.UserService;
import com.tafakkoor.e_learn.utils.Util;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class OAuthLoginController {
    private static final String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private final UserService userService;

    public OAuthLoginController(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, AuthUserRepository authUserRepository, UserService userService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
        this.userService = userService;
    }

    @GetMapping("/oauth_login")

    public String getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
//        oauth2AuthenticationUrls.put("LinkedIn", "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=86ecomlbchvzno&redirect_uri=http://localhost:8080/&state=987654321&scope=r_liteprofile%20r_emailaddress");
        model.addAttribute("urls", oauth2AuthenticationUrls);


        return "/oauth_login";
    }

    @GetMapping("/loginSuccess/**")
    public String getLoginInfo(@NotNull OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();
        System.out.println("userInfoEndpointUri = " + userInfoEndpointUri);
        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            String username = (String) userAttributes.get("sub");

            boolean userExist = userService.userExist(username);
            if (!userExist && userInfoEndpointUri.contains("google"))
                userService.saveGoogleUser(userAttributes);
            else if (!userExist && userInfoEndpointUri.contains("facebook"))
                userService.saveFacebookUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("github"))
//                userService.saveGithubUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("linkedin"))
//                userService.saveLinkedinUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("twitter"))
//                userService.saveTwitterUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("microsoft"))
//                userService.saveMicrosoftUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("yahoo"))
//                userService.saveYahooUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("okta"))
//                userService.saveOktaUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("salesforce"))
//                userService.saveSalesforceUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("paypal"))
//                userService.savePaypalUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("dropbox"))
//                userService.saveDropboxUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("spotify"))
//                userService.saveSpotifyUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("box"))
//                userService.saveBoxUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("amazon"))
//                userService.saveAmazonUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("discord"))
//                userService.saveDiscordUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("twitch"))
//                userService.saveTwitchUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("slack"))
//                userService.saveSlackUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("instagram"))
//                userService.saveInstagramUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("vk"))
//                userService.saveVkUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("line"))
//                userService.saveLineUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("weibo"))
//                userService.saveWeiboUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("douban"))
//                userService.saveDoubanUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("renren"))
//                userService.saveRenrenUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("kakao"))
//                userService.saveKakaoUser(userAttributes);
//            else if (!userExist && userInfoEndpointUri.contains("baidu"))
//                userService
        }
            return "redirect:/home";
        }

}
