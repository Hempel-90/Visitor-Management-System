package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die Company-Entity:
 *  - JPA-Annotationen für die Persistenz (Entity, Id, GeneratedValue, OneToMany, CascadeType)
 *  - Lombok-Annotationen für Getter, Setter, Konstruktoren
 *  - Java List für die Zuordnung von Besuchern
 */

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region Company Entity-----------------------------------------------------------------------------------------------

/*
 * Company Entity
 *
 * Repräsentiert ein Unternehmen im System:
 *  - id: Eindeutige Identifikationsnummer
 *  - companyName: Name des Unternehmens
 *  - active: Status, ob das Unternehmen aktiv ist
 *  - visitors: Liste aller Besucher, die diesem Unternehmen zugeordnet sind
 *
 * Technologien:
 *  - JPA für die Persistenz
 *  - Lombok zur Reduzierung von Boilerplate-Code
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private boolean active = true;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visitor> visitors;

}

//endregion Company Entity--------------------------------------------------------------------------------------------