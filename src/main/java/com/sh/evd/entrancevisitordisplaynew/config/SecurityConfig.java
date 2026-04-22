package com.sh.evd.entrancevisitordisplaynew.config;

//region Imports--------------------------------------------------------------------------------------------------------

import com.sh.evd.entrancevisitordisplaynew.entity.User;
import com.sh.evd.entrancevisitordisplaynew.repository.UserRepository;
// import com.sh.evd.entrancevisitordisplaynew.security.OidcUserPrincipal; // ❌ OIDC deaktiviert
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
// import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
// import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
// import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region SecurityConfig-------------------------------------------------------------------------------------------------

/*
 *  Security-Config:
 *  - OIDC / Microsoft Login DEAKTIVIERT
 *  - App läuft ohne Redirect direkt in UI
 *  - alte Logik bleibt auskommentiert erhalten
 */

@Slf4j
@Configuration
@Profile("!local")
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    //region Repository-------------------------------------------------------------------------------------------------

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //endregion Repository----------------------------------------------------------------------------------------------

    //region HttpSecurity Konfiguration---------------------------------------------------------------------------------

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //region ALT (OIDC / MICROSOFT LOGIN) - DEAKTIVIERT------------------------------------------------------------

        /*
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/profile-photo", "/api/profile-debug", "/api/test-photo").authenticated()
                .requestMatchers("/images/**", "/static/**", "/default-avatar.png").permitAll()
        );

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        http.oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/?login=true", true)
        );

        super.configure(http);
        setLoginView(http, com.sh.evd.entrancevisitordisplaynew.views.LoginView.class);
        */

        //endregion ALT-------------------------------------------------------------------------------------------------

        //region NEU (OHNE LOGIN / DIREKT ZUGANG)---------------------------------------------------------------------

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 🔥 alles frei → kein Redirect
        );

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        // ❌ KEIN oauth2Login
        // ❌ KEIN setLoginView
        // ❌ KEIN super.configure nötig für Login-Schutz

        //endregion NEU-----------------------------------------------------------------------------------------------

    }

    //endregion HttpSecurity Konfiguration------------------------------------------------------------------------------

    //region OIDC User Service (ALT - DEAKTIVIERT)------------------------------------------------------------------------

    /*
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final var oidcUserService = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);

            List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("OIDC_USER"))) {
                authorities.add(new SimpleGrantedAuthority("ROLE_OIDC_USER"));
            }

            oidcUser = new DefaultOidcUser(
                    authorities,
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );

            User dbUser = processUserRegistration(oidcUser);

            return new OidcUserPrincipal(oidcUser, dbUser);
        };
    }
    */

    //endregion OIDC User Service---------------------------------------------------------------------------------------

    //region Benutzerregistrierung / Datenbank (ALT - DEAKTIVIERT)-------------------------------------------------------

    /*
    @Transactional
    public User processUserRegistration(OidcUser oidcUser) {
        String entraId = oidcUser.getSubject();

        return this.userRepository.findByEntraUserIdWithRoles(entraId).orElseGet(() -> {
            User newUser = new User();
            newUser.setEntraUserId(entraId);
            newUser.setEmail(oidcUser.getEmail());

            String givenName = oidcUser.getGivenName() != null ? oidcUser.getGivenName() : "";
            String familyName = oidcUser.getFamilyName() != null ? oidcUser.getFamilyName() : "";
            newUser.setFullName((givenName + " " + familyName).trim());

            newUser.setActive(true);
            return this.userRepository.save(newUser);
        });
    }
    */

    //endregion Benutzerregistrierung / Datenbank-----------------------------------------------------------------------

    //region CORS Konfiguration-----------------------------------------------------------------------------------------

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //endregion CORS Konfiguration--------------------------------------------------------------------------------------
}

//endregion SecurityConfig----------------------------------------------------------------------------------------------
