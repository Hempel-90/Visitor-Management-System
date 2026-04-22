package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Abhängigkeiten:
 *  - OAuth2User: Repräsentiert Benutzerinformationen von OAuth2-Anbietern
 *  - AppUserPrincipal & AppUserInfo: Projektinterne Schnittstellen für Benutzerinformationen
 *  - GrantedAuthority: Rollen und Berechtigungen
 *  - Logger: Debug-Informationen
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppUserInfo;
import com.sh.evd.entrancevisitordisplaynew.security.AppUserPrincipal;
import com.sh.evd.entrancevisitordisplaynew.security.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region OAuth2UserPrincipalAdapter-------------------------------------------------------------------------------------

/*
 * Adapter zwischen OAuth2User und AppUserPrincipal:
 *  - Übersetzt OAuth2-Benutzerattribute in das projektinterne User-Interface
 *  - Extrahiert Name, E-Mail, Objekt-ID und Username
 *  - Stellt Authorities (Rollen) bereit
 */

public class OAuth2UserPrincipalAdapter implements AppUserPrincipal {

    private static final Logger log = LoggerFactory.getLogger(OAuth2UserPrincipalAdapter.class);

    private final OAuth2User oauth2User;
    private final AppUserInfo appUserInfo;

    public OAuth2UserPrincipalAdapter(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
        this.appUserInfo = createAppUserInfoFromOAuth2User(oauth2User);

        log.debug("Created OAuth2UserPrincipalAdapter for user: {} ({})",
                (Object) appUserInfo.getFullName(), (Object) appUserInfo.getEmail());
    }

    @Override
    public AppUserInfo getAppUser() {
        return appUserInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    //region Private Hilfsmethoden--------------------------------------------------------------------------------------

    private AppUserInfo createAppUserInfoFromOAuth2User(OAuth2User oauth2User) {

        if (log.isDebugEnabled()) {
            log.debug("OAuth2User attributes:");
            oauth2User.getAttributes().forEach((key, value) -> log.debug("  {}: {}",
                    (Object) key, (Object) value));
        }

        String objectId = getStringAttribute(oauth2User, "oid");
        String userPrincipalName = getStringAttribute(oauth2User, "userPrincipalName");
        String displayName = getStringAttribute(oauth2User, "displayName");
        String email = extractEmail(oauth2User);
        String firstName = getStringAttribute(oauth2User, "givenName");
        String lastName = getStringAttribute(oauth2User, "surname");

        if (isNullOrEmpty(displayName)) {
            if (!isNullOrEmpty(firstName) && !isNullOrEmpty(lastName)) {
                displayName = firstName + " " + lastName;
            } else if (!isNullOrEmpty(userPrincipalName)) {
                displayName = userPrincipalName.split("@")[0];
            }
        }

        return new OAuth2AppUserInfo(objectId, userPrincipalName, displayName, email);
    }

    private String extractEmail(OAuth2User oauth2User) {
        String email = getStringAttribute(oauth2User, "mail");
        if (!isNullOrEmpty(email)) return email;

        email = getStringAttribute(oauth2User, "email");
        if (!isNullOrEmpty(email)) return email;

        String upn = getStringAttribute(oauth2User, "userPrincipalName");
        if (!isNullOrEmpty(upn) && upn.contains("@")) return upn;

        return null;
    }

    private String getStringAttribute(OAuth2User oauth2User, String attributeName) {
        Object attribute = oauth2User.getAttribute(attributeName);
        return attribute != null ? attribute.toString() : null;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    //endregion Private Hilfsmethoden-----------------------------------------------------------------------------------

    //region OAuth2AppUserInfo------------------------------------------------------------------------------------------

    /*
     * Implementiert AppUserInfo für OAuth2-Benutzer:
     *  - Liefert userId, preferredUsername, fullName und E-Mail
     *  - Verwendet objectId oder userPrincipalName als ID
     */

    private static class OAuth2AppUserInfo implements AppUserInfo {
        private final String objectId;
        private final String userPrincipalName;
        private final String displayName;
        private final String email;

        public OAuth2AppUserInfo(String objectId, String userPrincipalName,
                                 String displayName, String email) {
            this.objectId = objectId;
            this.userPrincipalName = userPrincipalName;
            this.displayName = displayName;
            this.email = email;
        }

        @Override
        public UserId getUserId() {
            String id = objectId != null ? objectId : userPrincipalName;
            return UserId.of(id);
        }

        @Override
        public String getPreferredUsername() {
            return userPrincipalName != null ? userPrincipalName : email;
        }

        @Override
        public String getFullName() {
            return displayName != null ? displayName : getPreferredUsername();
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "OAuth2AppUserInfo{" +
                    "userId=" + getUserId() +
                    ", preferredUsername='" + getPreferredUsername() + '\'' +
                    ", fullName='" + getFullName() + '\'' +
                    ", email='" + getEmail() + '\'' +
                    '}';
        }
    }

    //endregion OAuth2AppUserInfo---------------------------------------------------------------------------------------
}

//endregion OAuth2UserPrincipalAdapter----------------------------------------------------------------------------------
