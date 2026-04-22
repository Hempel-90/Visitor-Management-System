package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/* Abstrakte Basisklasse für alle Entities.
 *  - Enthält ID, Erstellungs- und Änderungszeitpunkt
 *  - Automatische Pflege von createdAt / updatedAt
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region BaseEntity-----------------------------------------------------------------------------------------------------

/* Basisentity für alle Datenbankmodelle.
 *  - MappedSuperclass für Vererbung
 *  - Lombok für Getter/Setter
 */

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    // Eindeutige ID der Entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    // Zeitpunkt der Erstellung
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Zeitpunkt der letzten Änderung
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Vor dem Persistieren: createdAt setzen
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Vor dem Update: updatedAt setzen
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

//endregion BaseEntity--------------------------------------------------------------------------------------------------
