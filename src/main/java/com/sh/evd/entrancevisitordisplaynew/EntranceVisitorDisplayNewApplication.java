package com.sh.evd.entrancevisitordisplaynew;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die Spring Boot Anwendung:
 *  - SpringApplication: Startet die Spring Boot Anwendung
 *  - SpringBootApplication: Kennzeichnet die Hauptklasse als Spring Boot App
 *  - EnableScheduling: Aktiviert geplante Aufgaben (Scheduled Tasks)
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region EntranceVisitorDisplayNewApplication--------------------------------------------------------------------------

/*
 * EntranceVisitorDisplayNewApplication
 *
 * Hauptklasse der Spring Boot Anwendung für das Besucheranzeige-System:
 *  - Startet die Spring Boot Anwendung
 *  - Deaktiviert automatisch die Standard-Security-Konfiguration
 *  - Aktiviert Scheduling für geplante Aufgaben (z.B. VisitScheduleCleanupService)
 *
 * Technologien:
 *  - Spring Boot für einfache Konfiguration und Start
 *  - Spring Scheduling für Cron-Jobs
 */

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class // Security deaktivieren
})
@EnableScheduling // Aktiviert geplante Aufgaben
public class EntranceVisitorDisplayNewApplication {

    //region Main-Methode----------------------------------------------------------------------------------------------

    /*
     * main
     *
     * Einstiegspunkt der Anwendung
     * - Startet den Spring Application Context
     */
    public static void main(String[] args) {
        SpringApplication.run(EntranceVisitorDisplayNewApplication.class, args);
    }

    //endregion Main-Methode-------------------------------------------------------------------------------------------
}

//endregion EntranceVisitorDisplayNewApplication-----------------------------------------------------------------------
