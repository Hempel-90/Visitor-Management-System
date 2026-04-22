package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Abhängigkeiten:
 *  - OidcUser, OidcIdToken, OidcUserInfo: Repräsentation von OIDC-Benutzern und Tokeninformationen
 *  - User & Role: Lokale Datenbankentitäten
 *  - GrantedAuthority: Rollen und Berechtigungen
 *  - Collections & Streams: Hilfsmittel für Mapping und Filterung
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Role;
import com.sh.evd.entrancevisitordisplaynew.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region OidcUserPrincipal----------------------------------------------------------------------------------------------

/*
 * Wrapper für OidcUser, angereichert mit lokalen Daten:
 *  - Enthält Rollen und Berechtigungen aus der Datenbank
 *  - Delegiert alle OidcUser-Methoden an das Original
 *  - Stellt zusätzliche Methode getPermissions() bereit
 */

public record OidcUserPrincipal(OidcUser delegate, User dbUser) implements OidcUser {

    public OidcUserPrincipal {
        Objects.requireNonNull(delegate);
        Objects.requireNonNull(dbUser);
    }

    /**
     * Liefert eine Map aller Rollen des Benutzers (Role ID -> Role Name)
     * oder eine leere Map, wenn keine Rollen vorhanden sind.
     */
    public Map<Long, String> getPermissions() {
        if (dbUser.getRoles() == null) {
            return Map.of();
        }
        return dbUser.getRoles().stream()
                .collect(Collectors.toMap(Role::getId, Role::getName));
    }

    //region OidcUser-Delegation----------------------------------------------------------------------------------------

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    //endregion OidcUser-Delegation-------------------------------------------------------------------------------------
}

//endregion OidcUserPrincipal-------------------------------------------------------------------------------------------
