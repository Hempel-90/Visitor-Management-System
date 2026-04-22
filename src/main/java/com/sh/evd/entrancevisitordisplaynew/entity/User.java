package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/* Entity zur Modellierung eines Benutzers in der Anwendung.
 *  - Enthält grundlegende User-Informationen wie ID, Name, E-Mail und Aktiv-Status
 *  - Verknüpfung zu Rollen über Many-to-Many Beziehung
 */

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sh.evd.entrancevisitordisplaynew.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region UserEntity-----------------------------------------------------------------------------------------------------

/* User-Entity für die Persistenzschicht.
 *  - Basis: BaseEntity
 *  - Lombok für Getter, Setter und NoArgsConstructor
 */

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {

    // Eindeutige Benutzer-ID (externes System)
    @Column(name = "entra_user_id", nullable = false, unique = true, length = 255)
    private String entraUserId;

    // Vollständiger Name des Benutzers
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // E-Mail-Adresse des Benutzers (erforderlich)
    @NotNull(message = "Email ist erforderlich!")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Aktiv-Status des Benutzers (default: true)
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    // Rollen des Benutzers (Many-to-Many Beziehung)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}

//endregion UserEntity--------------------------------------------------------------------------------------------------
