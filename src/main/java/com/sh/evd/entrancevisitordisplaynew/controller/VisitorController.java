package com.sh.evd.entrancevisitordisplaynew.controller;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den VisitorController:
 *  - Entity-Klasse „Visitor" für Besucherobjekte
 *  - Service „VisitorService" für CRUD-Operationen auf Besucher
 *  - Spring Component-Annotation für die Bean-Verwaltung
 *  - Java-Utilities für Listenoperationen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.sh.evd.entrancevisitordisplaynew.service.VisitorService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region VisitorController----------------------------------------------------------------------------------------------

/*
 * Der VisitorController koordiniert die Kommunikation zwischen der View- und der Service-Schicht.
 *
 * Hauptfunktionen:
 *  - Abrufen aller Besucher oder einzelner Besucher nach ID
 *  - Suche nach Besuchern anhand des Namens
 *  - Erstellen oder Aktualisieren von Besucher-Daten
 *  - Löschen von Besuchern
 *
 * Technologien:
 *  - Spring Component für die Bean-Verwaltung durch den Spring-Container
 *  - Delegation der Geschäftslogik an den VisitorService
 */

@Component
public class VisitorController {

    // Service für die Geschäftslogik rund um Besucher
    private final VisitorService visitorService;

    // Konstruktor mit Dependency Injection für den VisitorService
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    //region Methoden---------------------------------------------------------------------------------------------------

    /*
     * Dieser Block enthält alle Methoden des VisitorControllers:
     *  - Alle Besucher oder einzelne Besucher nach ID abrufen
     *  - Besucher nach Namen suchen
     *  - Besucher erstellen oder aktualisieren
     *  - Besucher löschen
     */

    // Alle Besucher abrufen
    public List<Visitor> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    // Besucher nach ID abrufen
    public Optional<Visitor> getVisitorById(Long id) {
        return visitorService.getVisitorById(id);
    }

    // Besucher nach Namen suchen
    public List<Visitor> getVisitorsByName(String name) {
        return visitorService.getVisitorsByName(name);
    }

    // Besucher speichern oder aktualisieren
    public Visitor saveVisitor(Visitor visitor) {
        return visitorService.saveVisitor(visitor);
    }

    // Besucher löschen
    public void deleteVisitorById(Long id) {
        visitorService.deleteVisitorById(id);
    }

    //endregion Methoden------------------------------------------------------------------------------------------------
}

//endregion VisitorController-------------------------------------------------------------------------------------------