package com.sh.evd.entrancevisitordisplaynew.local;

//region SampleUsers----------------------------------------------------------------------------------------------------

/*
 *  Utility-Klasse mit vordefinierten Test- und Entwicklungsbenutzern.
 *  Enthält sowohl komplette LocalUser-Objekte als auch Konstanten für IDs und Benutzernamen.
 *  Nur für Entwicklungs- und Testzwecke, niemals in Produktion verwenden.
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppRoles;
import com.sh.evd.entrancevisitordisplaynew.security.UserId;

import java.util.List;
import java.util.UUID;

public final class SampleUsers {

    // Verhindert Instanziierung
    private SampleUsers() {
    }

    // Gemeinsames Passwort für alle Testbenutzer
    static final String SAMPLE_PASSWORD = "tops3cr3t";

    // Admin-Benutzer-ID
    public static final UserId ADMIN_ID = UserId.of(UUID.randomUUID().toString());

    // Admin-Benutzername
    public static final String ADMIN_USERNAME = "admin";

    // Admin-Benutzerobjekt mit Rollen ADMIN und USER
    static LocalUser ADMIN = LocalUser.builder()
            .preferredUsername(ADMIN_USERNAME)
            .fullName("Alice Administrator")
            .userId(ADMIN_ID)
            .password(SAMPLE_PASSWORD)
            .email("alice@example.com")
            .roles(AppRoles.ADMIN, AppRoles.USER)
            .build();

    // Standardbenutzer-ID
    public static final UserId USER_ID = UserId.of(UUID.randomUUID().toString());

    // Standardbenutzername
    public static final String USER_USERNAME = "user";

    // Standardbenutzerobjekt mit Rolle USER
    static final LocalUser USER = LocalUser.builder()
            .preferredUsername(USER_USERNAME)
            .fullName("Ursula User")
            .userId(USER_ID)
            .password(SAMPLE_PASSWORD)
            .email("ursula@example.com")
            .roles(AppRoles.USER)
            .build();

    // Unveränderliche Liste aller Testbenutzer
    static final List<LocalUser> ALL_USERS = List.of(USER, ADMIN);
}

//endregion SampleUsers-------------------------------------------------------------------------------------------------
