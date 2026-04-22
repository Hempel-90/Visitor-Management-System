package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Framework- und Projektabhängigkeiten:
 *  - CurrentUser, AppUserPrincipal, AppUserInfo: Zugriff auf authentifizierte Benutzer
 *  - BrowserCallable & PermitAll: Exponieren der Methode an Hilla-Clients
 *  - Spring Security: Rollen und Sicherheitsprüfungen
 *  - Nullable & NonNull: Annotations für null-sichere Datenübergabe
 *  - Collection: Speicherung von Authorities
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppUserInfo;
import com.sh.evd.entrancevisitordisplaynew.security.AppUserPrincipal;
import com.sh.evd.entrancevisitordisplaynew.security.CurrentUser;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region CurrentUserService---------------------------------------------------------------------------------------------

/*
 * Browser-callable Service für Hilla-Clients:
 *  - Stellt Informationen zum aktuell authentifizierten Benutzer bereit
 *  - Sicherer Zugriff auf AppUserPrincipal und AppUserInfo
 *  - Alle sensiblen Daten bleiben serverseitig
 *  - Direktzugriff für Java-Clients sollte über CurrentUser erfolgen
 */

@BrowserCallable
@PermitAll
class CurrentUserService {

    private final CurrentUser currentUser;

    CurrentUserService(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    //region DTO UserInfo-----------------------------------------------------------------------------------------------

    /*
     * Data Transfer Object für die Frontend-Nutzung:
     *  - Enthält nur sichere und sichtbare Benutzerinformationen
     *  - Felder: userId, preferredUsername, fullName, profileUrl, pictureUrl, email, zoneId, locale, authorities
     */

    public record UserInfo(@NonNull String userId,
                           @NonNull String preferredUsername,
                           @NonNull String fullName,
                           @Nullable String profileUrl,
                           @Nullable String pictureUrl,
                           @Nullable String email,
                           @NonNull String zoneId,
                           @NonNull String locale,
                           @NonNull Collection<String> authorities) {
    }

    //endregion DTO UserInfo--------------------------------------------------------------------------------------------

    //region Öffentliche Methoden---------------------------------------------------------------------------------------

    /*
     * Liefert die Informationen des aktuell angemeldeten Benutzers.
     *  - Nutzt CurrentUser.requirePrincipal() für sicheren Zugriff
     *  - Wandelt Authorities in Strings um
     *  - Schließt sensible Informationen aus
     */

    public @NonNull UserInfo getUserInfo() {
        var principal = currentUser.requirePrincipal();
        var user = principal.getAppUser();
        var authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new UserInfo(
                user.getUserId().toString(),
                user.getPreferredUsername(),
                user.getFullName(),
                user.getProfileUrl(),
                user.getPictureUrl(),
                user.getEmail(),
                user.getZoneId().toString(),
                user.getLocale().toString(),
                authorities
        );
    }

    //endregion Öffentliche Methoden------------------------------------------------------------------------------------
}

//endregion CurrentUserService------------------------------------------------------------------------------------------
