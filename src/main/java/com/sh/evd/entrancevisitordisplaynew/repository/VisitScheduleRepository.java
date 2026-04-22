package com.sh.evd.entrancevisitordisplaynew.repository;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für das VisitScheduleRepository:
 *  - JPA Repository für Standard-Datenbankoperationen
 *  - Entity-Klassen VisitSchedule, Visitor und Company
 *  - Spring Repository Annotation für die Bean-Registrierung
 *  - LocalDate für Datumssuchen
 *  - Java List für Rückgabewerte
 *  - Transaktionale Annotation für Löschoperationen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitScheduleRepository----------------------------------------------------------------------------------------

/*
 * VisitSchedule Repository
 *
 * Schnittstelle für CRUD-Operationen auf VisitSchedule-Entitäten:
 *  - Erweiterung von JpaRepository bietet Standardmethoden wie save, findAll, findById, delete
 *  - Methoden für gezielte Abfragen:
 *      - findByVisitor_Company: Alle Besuchspläne eines bestimmten Unternehmens
 *      - findByVisitor: Alle Besuchspläne eines bestimmten Besuchers
 *      - findByVisitStartDate: Alle Besuchspläne an einem bestimmten Tag (Mehrtagestermine berücksichtigen Startdatum)
 *      - deleteByVisitStartDateBefore: Automatische Löschung alter Besuche
 *
 * Technologien:
 *  - Spring Data JPA für Datenbankzugriff
 */

@Repository
public interface VisitScheduleRepository extends JpaRepository<VisitSchedule, Long> {

    // Alle Besuchspläne eines Unternehmens
    List<VisitSchedule> findByVisitor_Company(Company company);

    // Alle Besuchspläne eines bestimmten Besuchers
    List<VisitSchedule> findByVisitor(Visitor visitor);

    // Alle Besuchspläne, die an einem bestimmten Startdatum beginnen
    List<VisitSchedule> findByVisitDate(LocalDate visitStartDate);

    // Alte Besuche löschen (vor dem Cutoff-Datum)
    @Modifying
    @Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM VisitSchedule v WHERE v.visitDate < :cutoffDate")
    int deleteOldSchedulesBefore(LocalDate cutoffDate);
}

//endregion VisitScheduleRepository-------------------------------------------------------------------------------------
