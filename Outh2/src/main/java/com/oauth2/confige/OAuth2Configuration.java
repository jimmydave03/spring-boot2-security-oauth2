package com.oauth2.confige;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.oauth2.enums.Roles;
import com.oauth2.security.CustomAuthenticationEntryPoint;
import com.oauth2.security.CustomLogoutSuccessHandler;
import com.oauth2.security.CustomTokenEnhancer;

@Configuration
public class OAuth2Configuration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Autowired
        private CustomLogoutSuccessHandler customLogoutSuccessHandler;

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutUrl("/oauth/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .and()
                    .csrf()
                    .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                    .disable()																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
                    .headers()
                    .frameOptions().disable()
                    .and().sessionManagement()																																																																																																																	
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/oauth/token", "/api/users/add").permitAll()
                    .antMatchers(HttpMethod.GET,"/oauth/check_token","/oauth/token").permitAll()
                    .antMatchers("/oauth/**", "/api/**").authenticated()
                    .antMatchers("/resources/static/**").permitAll().anyRequest().permitAll();
        }

    }																								
    						
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private DataSource dataSource;
        
        @Autowired
        private PasswordEncoder passwordEncoder;
        
        @Value("${authentication.oauth.clientid}")
        private String clientId;
        
        @Value("${authentication.oauth.secret}")
        private String clientSecret;
        
        @Value("${authentication.oauth.tokenValidityInSeconds}")
        private int timeout;
        
        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        
        @Bean
        public TokenEnhancer tokenEnhancer() {
            return new CustomTokenEnhancer();
        }
        
        @Bean
        public DefaultAccessTokenConverter accessTokenConverter() {
            return new DefaultAccessTokenConverter();
        }
                
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints
                    .tokenStore(tokenStore())
                    .tokenEnhancer(tokenEnhancer())
                    .accessTokenConverter(accessTokenConverter())
                    .authenticationManager(authenticationManager);
        }
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()
                    .withClient(clientId)
                    .scopes("read", "write")
                    .authorities(Roles.ADMIN.name(), Roles.USER.name())
                    .authorizedGrantTypes("password", "refresh_token")
                    .secret("{noop}"+clientSecret)
                    .accessTokenValiditySeconds(timeout);
        }
        
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.checkTokenAccess("permitAll()");
        }
    }
}
