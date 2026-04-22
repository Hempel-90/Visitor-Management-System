package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die VisitSchedule-Entity:
 *  - JPA-Annotationen für Persistenz (Entity, Id, GeneratedValue, ManyToOne, JoinColumn, Column)
 *  - Lombok-Annotationen für Getter, Setter und Konstruktoren
 *  - Java Time API für Datum und Uhrzeit (LocalDate, LocalTime)
 */

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitSchedule Entity-------------------------------------------------------------------------------------------

/*
 * VisitSchedule Entity
 *
 * Repräsentiert einen Besuchsplan im System:
 *  - id: Eindeutige Identifikationsnummer
 *  - companyName: Name des Unternehmens, das besucht wird
 *  - purpose: Zweck des Besuchs
 *  - visitDate: Datum des Besuchs
 *  - arrivalTime: Ankunftszeit des Besuchers
 *  - important: Flag, ob der Besuch als wichtig markiert ist
 *  - visitor: Zugehöriger Besucher
 *
 * Technologien:
 *  - JPA für Persistenz
 *  - Lombok zur Reduzierung von Boilerplate-Code
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String purpose;
    private LocalDate visitDate;
    private LocalTime arrivalTime;

    @Column(name = "important", nullable = false)
    private boolean important = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitSchedule that = (VisitSchedule) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

//endregion VisitSchedule Entity----------------------------------------------------------------------------------------
