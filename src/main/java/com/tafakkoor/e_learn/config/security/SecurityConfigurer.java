package com.tafakkoor.e_learn.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true

)
@PropertySource("classpath:application.properties")
public class SecurityConfigurer implements WebSecurityConfigurer<WebSecurity> {
    public static final String[] WHITE_LIST = {
            "/css/**",
            "/js/**",
            "/img/**",
            "/home",
            "/auth/login",
            "oauth/linkedin/**",
//            "/auth/oauth_login",
            "/upload",
            "/auth/register",
//            "/auth/**",
            "/oauth_login",
            "/oauth2/**",
            "static/downloads/**",
//            "/loginSuccess",
            "/auth/forget-password",
            "/auth/reset-password"

    };
    @Autowired
    @Lazy
    @Qualifier("clientRegistrationRepository")
    private ClientRegistrationRepository clientRegistrationRepository;

    private static final List<String> clients = List.of("google", "facebook"/*, "github", "linkedin"*/);

    private static String CLIENT_PROPERTY_KEY
            = "spring.security.oauth2.client.registration.";

    private final Environment env;


    private final AuthUserUserDetailsService authUserUserDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public SecurityConfigurer(/*ClientRegistrationRepository clientRegistrationRepository,*/ Environment env, AuthUserUserDetailsService authUserUserDetailsService,
                              AuthenticationFailureHandler authenticationFailureHandler) {
//        this.clientRegistrationRepository = clientRegistrationRepository;
        this.env = env;
        this.authUserUserDetailsService = authUserUserDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    public void init(WebSecurity builder) throws Exception {

    }

    @Override
    public void configure(WebSecurity builder) throws Exception {
//        builder
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
//        if (client.equals("linkedin")) {
//            return linkedinClientRegistration();
//        }
        return null;
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
    authorizationRequestRepository() {

        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

//    @Bean
//    public HttpFirewall

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

//    private ClientRegistration linkedinClientRegistration() {
//        return ClientRegistration.withRegistrationId("linkedin")
//                .clientId("86ecomlbchvzno")
//                .clientSecret("hf4cXRQmDiDh77og")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
//                .scope("r_liteprofile", "r_emailaddress")
//                .authorizationUri("https://www.linkedin.com/oauth/v2/authorization")
//                .tokenUri("https://www.linkedin.com/oauth/v2/accessToken")
//                .userInfoUri("https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))")
//                .userNameAttributeName("id")
//                .clientName("LinkedIn")
//                .build();
//    }


    @Bean
    public OAuth2ClientContext oauth2ClientContext() {
        return new DefaultOAuth2ClientContext();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(WHITE_LIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )

//                .exceptionHandling(exceptionHandlingConfigurer ->
//                        exceptionHandlingConfigurer
//                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND))
//                                .accessDeniedHandler(accessDeniedHandler())
//                                .defaultAuthenticationEntryPointFor(
//                                        new HttpStatusEntryPoint(HttpStatus.NOT_FOUND),
//                                        new AntPathRequestMatcher("/**"))
//                )


                .oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(authorizedClientService())
                .loginPage("/oauth_login")
                .defaultSuccessUrl("/loginSuccess", true)
                .failureUrl("/auth/login?error=Authentication failed")
                .and()


                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/login")
                                .usernameParameter("uname")
                                .passwordParameter("pswd")
                                .defaultSuccessUrl("/home", false)
                                .failureHandler(authenticationFailureHandler)
                )


//                .formLogin(httpSecurityFormLoginConfigurer ->
//                        httpSecurityFormLoginConfigurer
//                                .loginPage("/auth/register")
//                                .loginProcessingUrl("/auth/register")
//                                .usernameParameter("username")
//                                .passwordParameter("password")
//                                .passwordParameter("confirmPassword")
//                                .passwordParameter("email")
//                                .defaultSuccessUrl("/home", false)
//                                .failureHandler(authenticationFailureHandler)
//                )

                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/auth/logout")
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID", "rememberME")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))

                )
                .userDetailsService(authUserUserDetailsService)
                .rememberMe(httpSecurityRememberMeConfigurer ->
                        httpSecurityRememberMeConfigurer
                                .rememberMeParameter("rememberMe")
                                .key("EWT$@WEFYG%H$ETGE@R!T#$HJYYT$QGRWHNJU%$TJRUYRHFRYFJRYUYRHD")
                                .tokenValiditySeconds(24 * 60 * 60)// default is 30 minutes
                                .rememberMeCookieName("rememberME")
                                .authenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                                    AuthUserUserDetails userDetails = (AuthUserUserDetails) authUserUserDetailsService.loadUserByUsername(authentication.getName());
                                    userDetails.setLastLogin(LocalDateTime.now());
                                    authUserUserDetailsService.save(userDetails.getAuthUser());
                                })
                );
        return http.build();
    }

    //    private AccessDeniedHandler accessDeniedHandler() {
//        return (httpServletRequest, httpServletResponse, e) -> {
//            if (httpServletResponse.getStatus() == HttpStatus.NOT_FOUND.value()) {
//                httpServletRequest.getRequestDispatcher("/errors/403.html").forward(httpServletRequest, httpServletResponse);
////                httpServletResponse.sendRedirect("/auth/login?error=Authentication failed");
//            } else {
//                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
//            }
//        };
//    }
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {

        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
    }


}
