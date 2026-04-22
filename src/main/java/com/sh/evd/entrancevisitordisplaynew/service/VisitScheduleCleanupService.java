package com.sh.evd.entrancevisitordisplaynew.service;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den VisitScheduleCleanupService:
 *  - Spring Framework Annotationen für Service, Scheduling und Transaktionen
 *  - Repositories und Services für Besucher und Besuchsplanung
 *  - Java Time API für Datumsermittlung (LocalDate)
 *  - Java Collections für Listenoperationen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.sh.evd.entrancevisitordisplaynew.repository.VisitScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitScheduleCleanupService-----------------------------------------------------------------------------------

/*
 * VisitScheduleCleanupService
 *
 * Verantwortlich für die regelmäßige Bereinigung alter Besuchsdaten:
 *  1. Löscht alte Besuchseinträge, die älter als 30 Tage sind
 *  2. Prüft alle Besucher und löscht diejenigen, die seit 180 Tagen keine Termine mehr hatten
 *
 * Technologien:
 *  - Spring Service für Business-Logik
 *  - Spring Scheduling zur automatischen Ausführung (cron)
 *  - Transaktionale Operationen für konsistente Datenbankänderungen
 */

@Service
public class VisitScheduleCleanupService {

    //region Member Variablen-------------------------------------------------------------------------------------------

    private final VisitScheduleRepository scheduleRepository; // Repository für Besuchsplan-Daten
    private final VisitorService visitorService;             // Service für Besucheroperationen

    private final int visitorRetentionDays = 180;           // Anzahl Tage, nach denen Besucher gelöscht werden

    //endregion Member Variablen----------------------------------------------------------------------------------------


    //region Konstruktor-------------------------------------------------------------------------------------------------

    /*
     * Konstruktor-Injektion für Repository und Service
     * Vorteil: einfache Testbarkeit und klare Abhängigkeiten
     */
    public VisitScheduleCleanupService(VisitScheduleRepository scheduleRepository,
                                       VisitorService visitorService) {
        this.scheduleRepository = scheduleRepository;
        this.visitorService = visitorService;
    }

    //endregion Konstruktor--------------------------------------------------------------------------------------------


    //region Methoden---------------------------------------------------------------------------------------------------

    /*
     * cleanupOldData
     *
     * Wird automatisch täglich um 2:00 Uhr ausgeführt (Cron):
     *  - Löscht alte Termine, die älter als 90 Tage sind
     *  - Löscht Besucher, die seit `visitorRetentionDays` keine Termine mehr hatten
     *
     * Transaktional: Alle Löschvorgänge erfolgen in einer DB-Transaktion
     */
    @Scheduled(cron = "0 0 2 * * ?") // täglich um 2:00 Uhr
    @Transactional
    public void cleanupOldData() {

        //region Alte Besuchseinträge löschen----------------------------------------------------------------------------

        LocalDate scheduleCutoff = LocalDate.now().minusDays(90); // Datumskriterium für alte Termine
        int deletedSchedules = scheduleRepository.deleteOldSchedulesBefore(scheduleCutoff);
        System.out.println("Alte Termine gelöscht bis: " + scheduleCutoff + " | Anzahl: " + deletedSchedules);

        //endregion Alte Besuchseinträge löschen------------------------------------------------------------------------

        //region Besucherprüfung und ggf. Löschen------------------------------------------------------------------------

        LocalDate visitorCutoff = LocalDate.now().minusDays(visitorRetentionDays); // Datumskriterium für Besucher
        List<Visitor> allVisitors = visitorService.getAllVisitors();

        for (Visitor visitor : allVisitors) {

            // Prüfen, ob Besucher noch Termine innerhalb der Retentionszeit hat
            boolean hasRecentVisit = visitorService.getVisitSchedulesForVisitor(visitor)
                    .stream()
                    .anyMatch(v -> v.getVisitDate() != null && !v.getVisitDate().isBefore(visitorCutoff));

            // Wenn keine aktuellen Termine existieren, Besucher löschen
            if (!hasRecentVisit) {
                visitorService.deleteVisitorById(visitor.getId());
                System.out.println("Besucher gelöscht: " + visitor.getName());
            }
        }


        //endregion Besucherprüfung und ggf. Löschen---------------------------------------------------------------------
    }

    //endregion Methoden------------------------------------------------------------------------------------------------
}

//endregion VisitScheduleCleanupService--------------------------------------------------------------------------------
