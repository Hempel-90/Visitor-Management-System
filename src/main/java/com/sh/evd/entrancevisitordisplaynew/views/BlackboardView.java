package com.sh.evd.entrancevisitordisplaynew.views;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die BlackboardView:
 *  - Entity-Klasse „VisitSchedule" für Besucherdaten
 *  - Controller „VisitScheduleController" für den Zugriff auf Besucherdaten
 *  - Vaadin-Komponenten für Layout, UI-Elemente, Polling und Navigation
 *  - Utility-Klassen für Datum- und Zeitformatierungen
 */

import com.sh.evd.entrancevisitordisplaynew.controller.VisitScheduleController;
import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region BlackboardView-------------------------------------------------------------------------------------------------

/*
 * Die BlackboardView zeigt eine Liste der heutigen Besucher in einem Scroll-Layout an.
 *
 * Hauptfunktionen:
 *  - Anzeige der Besucher in einer vertikal scrollbaren Liste
 *  - Automatische Aktualisierung alle 5 Sekunden
 *  - Besucher werden nach Unternehmen gruppiert
 *  - Aktuelle Zeit und Datum werden im Footer angezeigt
 *
 * Technologien:
 *  - Vaadin Flow Framework für die UI-Implementierung
 *  - Controller „VisitScheduleController" zur Datenabfrage
 *  - CSS-Styling über „blackboard-view.css"
 */

@AnonymousAllowed
@Route("visitors-today")
@PageTitle("Heutige Besucher")
@CssImport("./styles/blackboard-view.css")

public class BlackboardView extends VerticalLayout {

    //region Felder-----------------------------------------------------------------------------------------------------

    /*
     * Zentrale Controller- und UI-Komponenten:
     *  - visitScheduleController: Zugriff auf Besuchsdaten über den Controller-Layer
     *  - lastVisitorIds: Speichert die IDs der zuletzt angezeigten Besucher, um doppelte Einträge zu vermeiden
     */

    private final VisitScheduleController visitScheduleController;
    private Set<Long> lastVisitorIds = new HashSet<>();

    // Feldvariable für die Besucher-Liste
    private final Div visitorListContainer = new Div();
    private final Div visitorListInner = new Div();

    // Feldvariable für den Begrüßungs-Header
    private final H2 visitorsTodayListHeader = new H2("Heute begrüßen wir:");

    //endregion Felder--------------------------------------------------------------------------------------------------


    //region Konstruktor------------------------------------------------------------------------------------------------

    /*
     * Initialisiert die Ansicht und lädt die Besucher-Daten:
     *  - Setzt das Favicon
     *  - Erstellt Header, Footer und Besucheranzeige
     *  - Lädt die Besucher-Daten und sorgt für Live-Updates
     */

    // Hauptcontainer
    @Autowired
    public BlackboardView(VisitScheduleController visitScheduleController) {

        this.visitScheduleController = visitScheduleController;

        // Tab Icon
        UI.getCurrent().getPage().executeJs(
                "let f=document.querySelector('link[rel=\"icon\"]')||document.createElement('link');" +
                        "f.rel='icon';f.href=$0;document.head.appendChild(f);",
                "/frontend/images/favicon.ico"
        );

        addClassName("visitors-today-view");
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);


        //region Header-------------------------------------------------------------------------------------------------

        // Überschrift-Header
        H2 header1 = new H2("Herzlich willkommen!");
        header1.addClassName("visitors-today-header");

        // Begrüßungs-Header
        visitorsTodayListHeader.addClassName("visitors-today-list-header");

        // Container zur Anordnung des Scrollcontainers und Begrüßungstextes
        Div headerContainer = new Div();
        headerContainer.addClassName("visitors-today-header-container");

        //endregion Header----------------------------------------------------------------------------------------------


        //region Scroll-Container---------------------------------------------------------------------------------------

        // Scroll-Container außen
        visitorListContainer.addClassName("visitors-today-list-container");

        // Scroll-Container innen
        visitorListInner.addClassName("visitors-today-list-inner");

        visitorListContainer.add(visitorListInner);
        headerContainer.add(visitorsTodayListHeader, visitorListContainer);

        add(header1, headerContainer);

        // Polling alle 5 Sekunden zum Live-Update
        UI.getCurrent().setPollInterval(1000);
        UI.getCurrent().addPollListener(e -> {
            updateVisitors(visitorListInner);
            startAutoScroll(visitorListContainer, visitorListInner);
        });

        updateVisitors(visitorListInner);

        //endregion Scroll-Container------------------------------------------------------------------------------------


        //region Footer-------------------------------------------------------------------------------------------------

        // Footer
        Div footer = new Div();
        footer.addClassName("visitors-today-footer");

        // Datum und Uhrzeit im Footer
        Span dateSpan = new Span();
        dateSpan.addClassName("footer-date");

        // Infotext im Footer
        Span footerText = new Span("© 2026 Kevin Krummacker – Alle Rechte vorbehalten");
        footerText.addClassName("footer-text");

        footer.add(dateSpan, footerText);
        add(footer);

        // Datum regelmäßig aktualisieren
        UI.getCurrent().setPollInterval(1000);
        UI.getCurrent().addPollListener(e -> {
            String currentDateTime =
                    java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern(
                                    "EEEE, dd.MM.yyyy – HH:mm 'Uhr'",
                                    java.util.Locale.GERMAN));
            dateSpan.setText(currentDateTime);
        });

        //endregion Footer----------------------------------------------------------------------------------------------
    }

    //endregion Konstruktor---------------------------------------------------------------------------------------------


    //region updateVisitors---------------------------------------------------------------------------------------------

    /*
     * Aktualisiert die Liste der heutigen Besucher:
     *  - Holt die Besucherdaten über den Controller
     *  - Gruppiert Besucher nach Unternehmen
     *  - Aktualisiert die Ansicht im UI
     */

    private void updateVisitors(Div visitorListInner) {

        // Aktuelle Besucher abrufen
        LocalDate today = LocalDate.now();
        List<VisitSchedule> todayVisits = visitScheduleController.getScheduleByStartDate(today);

        // IDs der aktuellen Besucher
        Set<Long> currentIds = new HashSet<>();
        for (VisitSchedule v : todayVisits) {
            if (v.getVisitor() != null && v.getVisitor().getId() != null) {
                currentIds.add(v.getVisitor().getId());
            }
        }

        // Prüfen, ob ein Besucher gelöscht wurde
        if (!lastVisitorIds.isEmpty() && !currentIds.containsAll(lastVisitorIds)) {
            // Seite neu laden -> entfernt Klon & alten Inhalt
            UI.getCurrent().getPage().executeJs("location.reload();");
            return;
        }

        lastVisitorIds = currentIds;

        // Vorherige Animationen oder Klone im Scrollbereich bereinigen
        visitorListInner.getElement().executeJs(
                "const inner = this;" +
                        "try {" +
                        "  if (inner._frame) { cancelAnimationFrame(inner._frame); inner._frame = null; }" +
                        "  if (inner._cloneEl) {" +
                        "    inner._cloneEl.remove();" +
                        "    inner._cloneEl = null;" +
                        "    inner._cloned = false;" +
                        "  }" +
                        "} catch(e) { console.warn('Bereinigung fehlgeschlagen', e); }"
        );

        // Inhalte entfernen und neu befüllen
        visitorListInner.removeAll();

        // Wenn keine Besucher anderes Layout
        if (todayVisits.isEmpty()) {
            addClassName("no-visitors");
            visitorListContainer.setVisible(false);
            visitorsTodayListHeader.setVisible(false);
        } else {
            removeClassName("no-visitors");
            visitorListContainer.setVisible(true);
            visitorsTodayListHeader.setVisible(true);
        }

        // Besucher nach Unternehmen gruppieren (TreeMap -> alphabetisch sortiert)
        Map<String, Set<String>> visitorsByCompany = new TreeMap<>();
        for (VisitSchedule schedule : todayVisits) {
            String companyName = (schedule.getVisitor() != null && schedule.getVisitor().getCompany() != null)
                    ? schedule.getVisitor().getCompany().getCompanyName()
                    : (schedule.getCompanyName() != null && !schedule.getCompanyName().equals("-")
                    ? schedule.getCompanyName() : "");

            String visitorName = (schedule.getVisitor() != null)
                    ? schedule.getVisitor().getName()
                    : "";

            visitorsByCompany
                    .computeIfAbsent(companyName, k -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER))
                    .add(visitorName);
        }

        // Besuchergruppen rendern (Unternehmen + Namen)
        visitorsByCompany.forEach((companyName, visitorNames) -> {
            Div companyDiv = new Div();
            companyDiv.addClassName("visitor-entry");

            Span companySpan = new Span(companyName);
            companySpan.addClassName("visitor-company");
            companyDiv.add(companySpan);

            // Nur Besucher anzeigen, wenn Name nicht leer
            boolean hasRealVisitors = false;

            for (String nameStr : visitorNames) {
                if (nameStr != null && !nameStr.isBlank()) {
                    hasRealVisitors = true;
                    Span nameSpan = new Span(nameStr);
                    nameSpan.addClassName("visitor-name");
                    companyDiv.add(nameSpan);
                }
            }

            // Wenn keine Besucher → Company-Block optisch „allein" anzeigen
            if (!hasRealVisitors) {
                companyDiv.addClassName("company-only");
            }

            visitorListInner.add(companyDiv);
        });
    }

    //endregion updateVisitors------------------------------------------------------------------------------------------


    //region AutoScroll-------------------------------------------------------------------------------------------------

    /*
     * Startet die automatische Scroll-Funktion, um die Besucherliste fließend anzuzeigen:
     *  - Klont die Besucherliste für nahtloses Scrollen
     *  - Setzt die Geschwindigkeit der Animation
     */

    private void startAutoScroll(Div container, Div inner) {
        UI.getCurrent().getPage().executeJs(

                // Startet eine fließende vertikale Auto-Scroll-Animation mittels JavaScript
                // - Klont die Besucherliste für nahtlosen Loop
                // - Geschwindigkeit kann über "scrollSpeed" angepasst werden
                "const container = $0;" +
                        "const inner = $1;" +

                        // Berechne Höhen
                        "const containerHeight = container.clientHeight;" +
                        "const contentHeight = inner.scrollHeight;" +

                        // Scroll nur, wenn Inhalt höher ist als Container
                        // → Wenn kein Scroll nötig ist, Border entfernen
                        "if(contentHeight <= containerHeight) {" +
                        "  container.classList.remove('scrolling');" +
                        "  return;" +
                        "}" +

                        // Scroll aktiv → Border oben & unten anzeigen
                        "container.classList.add('scrolling');" +

                        // Prüfe, ob bereits geklont
                        "if(inner._cloned) return;" +
                        "inner._cloned = true;" +

                        // Klone Inhalt
                        "const clone = inner.cloneNode(true);" +
                        "clone.classList.add('clone');" +
                        "inner.parentNode.appendChild(clone);" +

                        "let pos = 0;" +
                        "const scrollSpeed = 1;" + // Geschwindigkeit anpassen
                        "function scrollLoop() {" +
                        "  pos += scrollSpeed;" +
                        "  inner.style.transform = `translateY(-${pos}px)`;" +
                        "  clone.style.transform = `translateY(-${pos}px)`;" +
                        "  if(pos >= inner.scrollHeight) pos = 0;" + // nahtlos weiterlaufen
                        "  requestAnimationFrame(scrollLoop);" +
                        "}" +
                        "scrollLoop();",
                container, inner
        );
    }

    //endregion AutoScroll----------------------------------------------------------------------------------------------


    //region Lifecycle--------------------------------------------------------------------------------------------------

    // Polling deaktivieren, wenn View geschlossen oder UI getrennt wird
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        UI.getCurrent().setPollInterval(-1);
    }

    //endregion Lifecycle-----------------------------------------------------------------------------------------------
}

//endregion BlackboardView----------------------------------------------------------------------------------------------