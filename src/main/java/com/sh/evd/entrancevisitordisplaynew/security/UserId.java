package com.sh.evd.entrancevisitordisplaynew.security;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Abhängigkeiten:
 *  - Serializable: Für die Übertragbarkeit über Streams oder Netzwerk
 *  - Objects: Hilfsmethoden für null-Prüfung und hashCode/equals
 */

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region UserId---------------------------------------------------------------------------------------------------------

/*
 * Immutable Value Object für Benutzer-IDs.
 *  - Kapselt eine String-ID für Typensicherheit
 *  - Serialisierbar, unveränderlich und mit konsistenter Gleichheitsprüfung
 *  - Factory-Methode `of()` für Instanziierung und Validierung
 */

public final class UserId implements Serializable {

    private final String userId;

    private UserId(String userId) {
        this.userId = requireNonNull(userId);
    }

    /**
     * Factory-Methode zur Erstellung einer UserId.
     *
     * @param userId die String-ID des Benutzers (nicht null)
     * @return neue UserId-Instanz
     * @throws IllegalArgumentException wenn ungültig
     */
    public static UserId of(String userId) {
        return new UserId(userId);
    }

    /**
     * Gibt die String-Repräsentation der UserId zurück.
     */
    @Override
    public String toString() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserId that = (UserId) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}

//endregion UserId------------------------------------------------------------------------------------------------------
