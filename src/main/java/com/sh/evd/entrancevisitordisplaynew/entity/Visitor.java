package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die Visitor-Entity:
 *  - JPA-Annotationen für Persistenz (Entity, Id, GeneratedValue, ManyToOne, OneToMany, JoinColumn)
 *  - Validation-Annotationen für Eingaben (@NotBlank, @Size)
 *  - Lombok-Annotationen für Getter, Setter und Konstruktoren
 *  - Java List für Besuchspläne
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.Size;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region Visitor Entity-------------------------------------------------------------------------------------------------

/*
 * Visitor Entity
 *
 * Repräsentiert einen Besucher im System:
 *  - id: Eindeutige Identifikationsnummer
 *  - name: Name des Besuchers, darf nicht leer sein und maximal 100 Zeichen
 *  - active: Status, ob der Besucher aktiv ist
 *  - company: Zugehöriges Unternehmen
 *  - schedules: Liste aller Besuchspläne des Besuchers
 *
 * Technologien:
 *  - JPA für die Persistenz
 *  - Validation-Annotationen zur Sicherstellung von Eingaben
 *  - Lombok zur Reduzierung von Boilerplate-Code
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Visitor {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 100)
   private String name;
   private boolean active = true;

   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name = "company_id")
   private Company company;

   @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<VisitSchedule> schedules;

}

//endregion Visitor Entity----------------------------------------------------------------------------------------------