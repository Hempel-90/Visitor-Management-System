package com.sh.evd.entrancevisitordisplaynew.controlcenter;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Security-Konfiguration für Produktionsumgebungen mit Vaadin Control Center (CC),
 * Kubernetes Deployment und OIDC/Keycloak Integration.
 */

import com.sh.evd.entrancevisitordisplaynew.controlcenter.OidcUserAdapter;
import com.vaadin.controlcenter.starter.idm.IdentityManagementConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region ControlCenterSecurityConfig------------------------------------------------------------------------------------

/*
 *  Security-Config für Produktionsumgebung:
 *  - Aktiviert bei Kubernetes + prod Profil
 *  - Nutzt Vaadin Control Center Identity Management
 *  - Mappt OIDC Benutzer auf eigene App-Sicherheitsmodelle
 */
@EnableWebSecurity
@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
class ControlCenterSecurityConfig extends IdentityManagementConfiguration {

    //region OidcUserService Bean---------------------------------------------------------------------------------------

    /*
     *  Erstellt und konfiguriert einen OidcUserService mit Custom Mapping:
     *  - Wandelt Keycloak OIDC User in eigene AppUserModelle
     */
    @Bean
    OidcUserService oidcUserService() {
        var userService = new OidcUserService();
        userService.setOidcUserMapper(ControlCenterSecurityConfig::mapOidcUser);
        return userService;
    }

    //endregion OidcUserService Bean------------------------------------------------------------------------------------

    //region OIDC Mapping-----------------------------------------------------------------------------------------------

    /*
     *  Wandelt OIDC UserRequest + UserInfo in OidcUserAdapter:
     *  - DefaultOidcUser erstellen
     *  - Mit App-spezifischem Adapter umhüllen
     *  - Einheitliche Schnittstelle für App-Sicherheitsmodell
     */
    private static OidcUser mapOidcUser(OidcUserRequest userRequest, OidcUserInfo userInfo) {
        var authorities = mapAuthorities(userRequest, userInfo);
        var providerDetails = userRequest.getClientRegistration().getProviderDetails();
        var userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();

        var oidcUser = StringUtils.hasText(userNameAttributeName)
                ? new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo, userNameAttributeName)
                : new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo);

        return new OidcUserAdapter(oidcUser);
    }

    //endregion OIDC Mapping--------------------------------------------------------------------------------------------
}

//endregion ControlCenterSecurityConfig---------------------------------------------------------------------------------
