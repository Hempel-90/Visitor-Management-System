package com.sh.evd.entrancevisitordisplaynew.dto;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für das VisitScheduleDto:
 *  - LocalDate und LocalTime für Datum und Uhrzeit des Besuchs
 *  - Lombok @Data für Getter, Setter, toString, equals und hashCode
 */

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitScheduleDto-----------------------------------------------------------------------------------------------

@Data

/*
 * VisitScheduleDto
 *
 * Data Transfer Object zur Übertragung von Besuchsdaten zwischen Client und Server.
 * Enthält alle relevanten Informationen für die Erstellung oder Anzeige eines Besuchsplans:
 *  - visitorId, visitorName: Informationen zum Besucher
 *  - companyName: Unternehmen des Besuchers
 *  - purpose: Besuchszweck
 *  - visitDate, arrivalTime: Zeitpunkt des Besuchs
 */

public class VisitScheduleDto {

    private Long visitorId;
    private String visitorName;
    private String companyName;
    private String purpose;
    private LocalDate visitDate;
    private LocalTime arrivalTime;

}

//endregion VisitScheduleDto--------------------------------------------------------------------------------------------
