package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Abhängigkeiten:
 *  - UserId: Wertobjekt für die eindeutige Benutzer-ID
 *  - ZoneId & Locale: für Zeitzonen- und Lokalisierungsinformationen
 *  - Nullable: für optionale Rückgabewerte
 */

import com.sh.evd.entrancevisitordisplaynew.security.UserId;
import org.jspecify.annotations.Nullable;

import java.time.ZoneId;
import java.util.Locale;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region AppUserInfo----------------------------------------------------------------------------------------------------

/*
 * Schnittstelle zur einheitlichen Abfrage von Benutzerinformationen.
 *  - Zugriff auf User-ID, bevorzugten Benutzernamen und Vollständigen Namen
 *  - Optional: Profil-URL, Profilbild, E-Mail-Adresse
 *  - Zeit- und Lokalisierungsinformationen
 */

public interface AppUserInfo {

     // Eindeutige Benutzer-ID innerhalb der Anwendung.
     // @return UserId (niemals null)
    UserId getUserId();

     // Bevorzugter Benutzername für Anzeigezwecke.
     // @return bevorzugter Benutzername (niemals null)
    String getPreferredUsername();

     // Vollständiger Name des Benutzers. Fallback: bevorzugter Benutzername.
     // @return Vollständiger Name (niemals null)
    default String getFullName() {
        return getPreferredUsername();
    }

     // URL zur Benutzerprofilseite.
     // @return Profil-URL oder null, falls nicht verfügbar
    default @Nullable String getProfileUrl() {
        return null;
    }

     // URL zum Profilbild oder Avatar.
     // @return Bild-URL oder null, falls nicht verfügbar
    default @Nullable String getPictureUrl() {
        return null;
    }

     // E-Mail-Adresse des Benutzers.
     // @return E-Mail oder null, falls nicht verfügbar
    default @Nullable String getEmail() {
        return null;
    }

     //Bevorzugte Zeitzone des Benutzers.
     //@return ZoneId (niemals null)
    default ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

     // Bevorzugte Locale des Benutzers.
     // @return Locale (niemals null)
    default Locale getLocale() {
        return Locale.getDefault();
    }
}

//endregion AppUserInfo-------------------------------------------------------------------------------------------------
