package com.sh.evd.entrancevisitordisplaynew.repository;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für das VisitRepository:
 *  - JPA Repository für Standard-Datenbankoperationen
 *  - Entity-Klassen Visitor und Company
 *  - Spring Repository Annotation für die Bean-Registrierung
 *  - Java List für Rückgabewerte
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitRepository------------------------------------------------------------------------------------------------

/*
 * Visitor Repository
 *
 * Schnittstelle für CRUD-Operationen auf Visitor-Entitäten:
 *  - Erweiterung von JpaRepository bietet Standardmethoden wie save, findAll, findById, delete
 *  - Methoden für Zählungen und gezielte Suchen:
 *      - countByName: Zählt Besucher anhand des Namens
 *      - countByNameAndCompany: Zählt Besucher eines bestimmten Unternehmens
 *      - findByCompany: Gibt alle Besucher eines Unternehmens zurück
 *      - findByName: Gibt alle Besucher mit bestimmtem Namen zurück
 *      - findByNameAndCompany: Gibt einen Besucher anhand von Name und Unternehmen zurück
 *
 * Technologien:
 *  - Spring Data JPA für Datenbankzugriff
 */

@Repository
public interface VisitRepository extends JpaRepository<Visitor, Long> {

    long countByName(String name);
    long countByNameAndCompany(String name, Company company);

    List<Visitor> findByCompany(Company company);
    List<Visitor> findByName(String name);

    Visitor findByNameAndCompany(String name, Company company);

}

//endregion VisitRepository---------------------------------------------------------------------------------------------
