package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Abhängigkeiten:
 *  - Principal: Sicherheits-Principal für Authentifizierungsframeworks
 *  - GrantedAuthority & Collection: Zugriff auf Rollen/Authorities
 */

import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region AppUserPrincipal-----------------------------------------------------------------------------------------------

/*
 * Interface für Sicherheits-Principals, die Zugriff auf die App-spezifischen Benutzerinformationen bieten.
 *  - Einheitlicher Zugriff auf User-Datenmodell
 *  - Erweiterung von Principal für Standard-Security-Integration
 *  - Authentifizierte Principals sollen diese Schnittstelle implementieren
 */

public interface AppUserPrincipal extends Principal {

     // Liefert die Anwendungsspezifischen Benutzerinformationen.
     // @return AppUserInfo-Objekt (niemals null)
    AppUserInfo getAppUser();

     // Gibt den Namen des Principals zurück (standardmäßig die User-ID als String).
     // @return Name des Principals (niemals null)
    @Override
    default String getName() {
        return getAppUser().getUserId().toString();
    }

     // Liefert die Authorities/Rollen des Benutzers.
     // @return unveränderliche Collection von GrantedAuthority (niemals null)
    Collection<? extends GrantedAuthority> getAuthorities();
}

//endregion AppUserPrincipal--------------------------------------------------------------------------------------------
