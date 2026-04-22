package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Framework- und Projektabhängigkeiten:
 *  - Spring Security: Zugriff auf Authentifizierungsinformationen
 *  - OAuth2: Unterstützung für OAuth2-Authentifizierung
 *  - Logger: Protokollierung
 *  - Optional: Sicherer Rückgabetyp für null-sichere Methoden
 */

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region CurrentUser----------------------------------------------------------------------------------------------------

/*
 * Component zur einfachen Abfrage des aktuell angemeldeten Benutzers:
 *  - get(): Optional<AppUserInfo> zurückgeben
 *  - getPrincipal(): Optional<AppUserPrincipal> zurückgeben
 *  - require(): Liefert Benutzer oder wirft Exception
 */

@Component
public class CurrentUser {

    private static final Logger log = LoggerFactory.getLogger(CurrentUser.class);

    public CurrentUser() {
    }

    //region Öffentliche Methoden---------------------------------------------------------------------------------------

    public Optional<AppUserInfo> get() {
        return getPrincipal().map(AppUserPrincipal::getAppUser);
    }

    public Optional<AppUserPrincipal> getPrincipal() {
        return Optional.ofNullable(
                getPrincipalFromAuthentication(SecurityContextHolder.getContext().getAuthentication()));
    }

    public AppUserInfo require() {
        return get().orElseThrow(() -> new AuthenticationCredentialsNotFoundException("No current user"));
    }

    public AppUserPrincipal requirePrincipal() {
        return getPrincipal().orElseThrow(() -> new AuthenticationCredentialsNotFoundException("No current user"));
    }

    //endregion Öffentliche Methoden------------------------------------------------------------------------------------

    //region Private Methoden-------------------------------------------------------------------------------------------

    private @Nullable AppUserPrincipal getPrincipalFromAuthentication(@Nullable Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        var principal = authentication.getPrincipal();

        if (principal instanceof AppUserPrincipal appUserPrincipal) {
            return appUserPrincipal;
        }

        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            log.debug("Creating OAuth2UserPrincipalAdapter for user: {}",
                    (Object) oauth2User.getAttribute("userPrincipalName"));
            return new OAuth2UserPrincipalAdapter(oauth2User);
        }

        log.warn("Unexpected principal type: {}", principal.getClass().getName());
        return null;
    }

    //endregion Private Methoden----------------------------------------------------------------------------------------
}

//endregion CurrentUser-------------------------------------------------------------------------------------------------
