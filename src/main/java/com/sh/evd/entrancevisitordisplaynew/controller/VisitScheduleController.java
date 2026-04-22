package com.sh.evd.entrancevisitordisplaynew.controller;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den VisitScheduleController:
 *  - DTO-Klasse „VisitScheduleDto" für die Datenübertragung beim Erstellen von Besuchsplänen
 *  - Entity-Klassen „VisitSchedule" und „Visitor" für Besuchsdaten
 *  - Services „VisitScheduleService" und „VisitorService" für CRUD-Operationen
 *  - Spring Component-Annotation für die Bean-Verwaltung
 *  - Java Collections für Listen und Datumsverwaltung
 */

import com.sh.evd.entrancevisitordisplaynew.dto.VisitScheduleDto;
import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.sh.evd.entrancevisitordisplaynew.service.VisitScheduleService;
import com.sh.evd.entrancevisitordisplaynew.service.VisitorService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitScheduleController----------------------------------------------------------------------------------------

/*
 * Der VisitScheduleController koordiniert die Kommunikation zwischen der View- und der Service-Schicht.
 *
 * Hauptfunktionen:
 *  - Abrufen aller Besuchspläne oder einzelner Pläne nach ID
 *  - Abrufen der Besuchspläne eines bestimmten Besuchers
 *  - Erstellen oder Aktualisieren von Besuchsplänen
 *  - Löschen von Besuchsplänen
 *
 * Technologien:
 *  - Spring Component für die Bean-Verwaltung durch den Spring-Container
 *  - Delegation der Geschäftslogik an den VisitScheduleService
 */

@Component
public class VisitScheduleController {

    private final VisitScheduleService visitScheduleService;
    private final VisitorService visitorService;

    public VisitScheduleController(VisitScheduleService visitScheduleService, VisitorService visitorService) {
        this.visitScheduleService = visitScheduleService;
        this.visitorService = visitorService;
    }

    //region Methoden---------------------------------------------------------------------------------------------------

    /*
     * Dieser Block enthält alle Methoden des VisitScheduleControllers:
     *  - Alle Besuchspläne abrufen
     *  - Besuchspläne eines bestimmten Besuchers abrufen
     *  - Besuchsplan nach ID abrufen
     *  - Besuchsplan speichern oder aktualisieren
     *  - Besuchsplan aus DTO erstellen (inkl. Mehrtageszeitraum)
     *  - Besuchsplan löschen
     */

    // Alle Besuchspläne abrufen
    public List<VisitSchedule> findAllVisitSchedules() {
        return visitScheduleService.findAllVisitSchedules();
    }

    // Besuchspläne eines bestimmten Besuchers abrufen
    public List<VisitSchedule> findByVisitor(Visitor visitor) {
        return visitScheduleService.findByVisitor(visitor);
    }

    // Besuchsplan nach ID abrufen
    public Optional<VisitSchedule> getScheduleById(Long id) {
        return visitScheduleService.getScheduleById(id);
    }

    // Heutige Besuchspläne abrufen
    public List<VisitSchedule> getScheduleByStartDate(LocalDate date) {
        return visitScheduleService.getScheduleByStartDate(date);
    }

    // Besuchsplan speichern oder aktualisieren
    public VisitSchedule saveSchedule(VisitSchedule schedule) {
        return visitScheduleService.saveSchedule(schedule);
    }

    // Besuchsplan aus DTO erstellen (inkl. Mehrtageszeitraum)
    public List<VisitSchedule> createFromDto(VisitScheduleDto visitScheduleDto) {
        Visitor visitor = visitorService.getVisitorById(visitScheduleDto.getVisitorId())
                .orElseThrow(() -> new IllegalArgumentException("Besucher nicht gefunden"));

        LocalDate start = visitScheduleDto.getVisitDate();
        LocalDate end = visitScheduleDto.getVisitDate() != null ? visitScheduleDto.getVisitDate() : start;

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Enddatum darf nicht vor Startdatum liegen");
        }

        List<VisitSchedule> savedSchedules = new ArrayList<>();

        // Für jeden Tag im Zeitraum einen eigenen Eintrag erstellen
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            VisitSchedule schedule = new VisitSchedule();
            schedule.setVisitor(visitor);
            schedule.setPurpose(visitScheduleDto.getPurpose());
            schedule.setVisitDate(date);
            schedule.setArrivalTime(visitScheduleDto.getArrivalTime());
            savedSchedules.add(visitScheduleService.saveSchedule(schedule));
        }

        return savedSchedules;
    }

    // Besuchsplan löschen
    public void deleteScheduleById(Long id) {
        visitScheduleService.deleteScheduleById(id);
    }

    //endregion Methoden------------------------------------------------------------------------------------------------
}

//endregion VisitScheduleController------------------------------------------------------------------------------------