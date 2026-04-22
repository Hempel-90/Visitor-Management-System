package com.sh.evd.entrancevisitordisplaynew.security;

//region AppRoles-------------------------------------------------------------------------------------------------------

/* Utility-Klasse zur zentralen Verwaltung der Rollenbezeichnungen innerhalb der Anwendung */

public final class AppRoles {

    // Verhindert die Instanziierung
    private AppRoles() {
    }

    // Administratorrolle mit erweiterten Rechten
    public static final String ADMIN = "ADMIN";

    // Standardbenutzerrolle
    public static final String USER = "LOCAL_USER";
}

//endregion AppRoles----------------------------------------------------------------------------------------------------
