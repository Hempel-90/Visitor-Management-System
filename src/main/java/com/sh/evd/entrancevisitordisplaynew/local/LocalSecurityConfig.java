package com.sh.evd.entrancevisitordisplaynew.local;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Lokale Entwicklungs-Sicherheitskonfiguration für Spring + Vaadin.
 * Stellt einen SecurityFilterChain und einen UserDetailsService für Testbenutzer bereit.
 */

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region LocalSecurityConfig--------------------------------------------------------------------------------------------

/*
 *  Konfiguriert Spring Security für die lokale Entwicklungsumgebung.
 *  - Definiert Zugriffsbeschränkungen für bestimmte Pfade
 *  - Ignoriert CSRF für statische VAADIN-Ressourcen
 *  - Bindet LoginView und UserDetailsService für SampleUsers ein
 */

@EnableWebSecurity
@Configuration
@Import({VaadinAwareSecurityContextHolderStrategyConfiguration.class})
@Profile("local")
class LocalSecurityConfig {

    // Logger für Warnmeldungen
    private static final Logger log = LoggerFactory.getLogger(LocalSecurityConfig.class);

    // Konstruktor
    LocalSecurityConfig() {
        log.warn("Using DEVELOPMENT security configuration. This should not be used in production environments!");
    }

    //region SecurityFilterChain----------------------------------------------------------------------------------------

    // Security Filter Chain für Entwicklungsumgebung
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/profile/**").hasRole("USER")     // Zugriff nur für USER
                        .requestMatchers("/dailylogin/**").hasRole("USER")  // Zugriff nur für USER
                        .requestMatchers("/VAADIN/**").permitAll()          // statische Ressourcen offen
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/VAADIN/**")  // CSRF für VAADIN ignorieren
                )
                .with(VaadinSecurityConfigurer.vaadin(),
                        configurer ->
                                configurer.loginView(LocalLoginView.LOGIN_PATH)); // LoginView konfigurieren

        return http.build();
    }

    //endregion SecurityFilterChain-------------------------------------------------------------------------------------

    //region UserDetailsService-----------------------------------------------------------------------------------------

    // Liefert UserDetailsService mit SampleUsers für Entwicklungszwecke
    @Bean
    UserDetailsService userDetailsService() {
        return new LocalUserDetailsService(SampleUsers.ALL_USERS);
    }

    //endregion UserDetailsService--------------------------------------------------------------------------------------

}

//endregion LocalSecurityConfig-----------------------------------------------------------------------------------------
