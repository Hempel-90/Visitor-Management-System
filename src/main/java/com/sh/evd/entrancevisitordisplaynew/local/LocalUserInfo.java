package com.sh.evd.entrancevisitordisplaynew.local;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Implementierung von AppUserInfo für Entwicklungs- und Testbenutzer.
 * Enthält alle notwendigen Benutzerinformationen wie IDs, Namen, E-Mail, Profil-URLs, Zeitzone und Locale.
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppUserInfo;
import com.sh.evd.entrancevisitordisplaynew.security.UserId;
import org.jspecify.annotations.Nullable;

import java.time.ZoneId;
import java.util.Locale;

import static java.util.Objects.requireNonNull;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region LocalUserInfo--------------------------------------------------------------------------------------------------

/* Immutable Record, das AppUserInfo für LocalUser bereitstellt.
 * Prüft notwendige Felder auf null und stellt alle Methoden der Schnittstelle bereit.
 */

record LocalUserInfo(UserId userId, String preferredUsername, String fullName,
                     @Nullable String profileUrl, @Nullable String pictureUrl,
                     @Nullable String email, ZoneId zoneId, Locale locale)
        implements AppUserInfo {

    // Konstruktor mit null-Prüfungen
    LocalUserInfo {
        requireNonNull(userId);
        requireNonNull(preferredUsername);
        requireNonNull(fullName);
        requireNonNull(zoneId);
        requireNonNull(locale);
    }

    // Benutzer-ID
    @Override
    public UserId getUserId() {
        return userId;
    }

    // Bevorzugter Benutzername
    @Override
    public String getPreferredUsername() {
        return preferredUsername;
    }

    // Vollständiger Name
    @Override
    public String getFullName() {
        return fullName;
    }

    // Profil-URL
    @Override
    public @Nullable String getProfileUrl() {
        return profileUrl;
    }

    // Profilbild-URL
    @Override
    public @Nullable String getPictureUrl() {
        return pictureUrl;
    }

    // E-Mail
    @Override
    public @Nullable String getEmail() {
        return email;
    }

    // Zeitzone
    @Override
    public ZoneId getZoneId() {
        return zoneId;
    }

    // Locale
    @Override
    public Locale getLocale() {
        return locale;
    }
}

//endregion LocalUserInfo-----------------------------------------------------------------------------------------------
