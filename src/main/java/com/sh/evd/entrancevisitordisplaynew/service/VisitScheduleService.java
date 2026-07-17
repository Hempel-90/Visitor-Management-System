package com.sh.evd.entrancevisitordisplaynew.service;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den VisitScheduleService:
 *  - VisitSchedule-, Visitor- und Company-Entity für Geschäftslogik
 *  - VisitScheduleRepository für CRUD-Operationen auf Besuchspläne
 *  - Optional für sichere Rückgaben einzelner Besuchspläne
 *  - LocalDate für Datumsabfragen
 *  - Spring Service Annotation zur Registrierung als Service-Komponente
 *  - List für Rückgabewerte von mehreren Besuchsplänen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.sh.evd.entrancevisitordisplaynew.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitScheduleService-------------------------------------------------------------------------------------------

/*
 * VisitSchedule Service
 *
 * Verantwortlich für die Geschäftslogik rund um Besuchspläne:
 *  - CRUD-Operationen auf VisitSchedule-Entitäten
 *  - Abfragen nach Besucher, Firma oder Startdatum
 *  - Kapselt die Repository-Zugriffe
 *
 * Technologien:
 *  - Spring Service für Service-Layer-Komponente
 *  - Nutzung von VisitScheduleRepository für Datenbankzugriff
 */

@Service
public class VisitScheduleService {

    private final VisitScheduleRepository visitScheduleRepository;

    public VisitScheduleService(VisitScheduleRepository visitScheduleRepository) {
        this.visitScheduleRepository = visitScheduleRepository;
    }

    // Alle Besuchspläne abrufen
    public List<VisitSchedule> findAllVisitSchedules() {
        return visitScheduleRepository.findAll();
    }

    // Besuchspläne eines bestimmten Besuchers abrufen
    public List<VisitSchedule> findByVisitor(Visitor visitor) {
        return visitScheduleRepository.findByVisitor(visitor);
    }

    // Einzelnen Besuchsplan nach ID abrufen
    public Optional<VisitSchedule> getScheduleById(Long id){
        return visitScheduleRepository.findById(id);
    }

    // Besuchspläne, die an einem bestimmten Startdatum beginnen
    public List<VisitSchedule> getScheduleByStartDate(LocalDate startDate){
        return visitScheduleRepository.findByVisitDate(startDate);
    }

    // Besuchsplan speichern oder aktualisieren
    public VisitSchedule saveSchedule(VisitSchedule schedule){
        return visitScheduleRepository.save(schedule);
    }

    // Besuchsplan löschen nach ID
    public void deleteScheduleById(Long id){
        visitScheduleRepository.deleteById(id);
    }

}

//endregion VisitScheduleService----------------------------------------------------------------------------------------
