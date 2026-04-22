package com.sh.evd.entrancevisitordisplaynew.local;

//region Imports--------------------------------------------------------------------------------------------------------

/* In-Memory UserDetailsService für Entwicklungs- und Testumgebungen.
 * Stellt eine Sammlung von LocalUsern bereit und ermöglicht Lookup nach bevorzugtem Benutzernamen.
 */

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region LocalUserDetailsService----------------------------------------------------------------------------------------

/* Einfache Implementierung von UserDetailsService für die Entwicklung.
 * Speichert Benutzer in einem Map, Lookup erfolgt über preferredUsername.
 */

final class LocalUserDetailsService implements UserDetailsService {

    // Map für schnellen Lookup der Benutzer nach preferredUsername
    private final Map<String, UserDetails> userByUsername;

    // Konstruktor: Benutzer in die Map einfügen
    LocalUserDetailsService(Collection<LocalUser> users) {
        userByUsername = new HashMap<>();
        users.forEach(user -> userByUsername.put(user.getAppUser().getPreferredUsername(), user));
    }

    // Lädt Benutzer anhand des Benutzernamens
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userByUsername.get(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

//endregion LocalUserDetailsService-------------------------------------------------------------------------------------
