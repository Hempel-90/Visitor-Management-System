package com.sh.evd.entrancevisitordisplaynew.views;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die MainView:
 *  - Entity-Klassen (Company, Visitor, VisitSchedule)
 *  - Controller für den Zugriff auf die Geschäftslogik
 *  - Vaadin-Komponenten für das UI (Grid, Buttons, Dialoge, Layouts usw.)
 *  - Utility-Klassen für Datum, Zeit und Formatierung
 *  - Session-Handling und Routing
 */

import com.sh.evd.entrancevisitordisplaynew.controller.CompanyController;
import com.sh.evd.entrancevisitordisplaynew.controller.VisitScheduleController;
import com.sh.evd.entrancevisitordisplaynew.controller.VisitorController;
import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region MainView-------------------------------------------------------------------------------------------------------

/*
 * MainView ist die zentrale Verwaltungsoberfläche der Besucherübersicht.
 *
 * Funktionen:
 *  - Anzeige aller geplanten Besuche (Grid mit Sortierung, Filterung und Markierungen)
 *  - Erstellung, Bearbeitung und Löschung von Besuchseinträgen
 *  - Filterung nach Datum, Besuchername, Firma und Zweck
 *  - Markierung von "wichtigen" Besuchen mit optischer Hervorhebung
 *  - Navigation zu anderen Modulen (Firmen, Kalender)
 *
 * Technologien:
 *  - Vaadin Flow UI Framework
 *  - Controller (VisitScheduleController, VisitorController, CompanyController)
 *  - CSS-Styling über „main-view.css"
 */

@PermitAll
@Route("mainview")
@PageTitle("Besuchsübersicht")
@CssImport("./styles/main-view.css")
public class MainView extends VerticalLayout {

    //region Felder-----------------------------------------------------------------------------------------------------

    /*
     * Zentrale Controller- und UI-Komponenten:
     *  - Controller: Zugriff auf Geschäftslogik über den Controller-Layer
     *  - Grid: Anzeige aller Besuche in Tabellenform
     *  - currentItems: Hält den aktuell im Grid angezeigten Stand (berücksichtigt aktive Filter)
     *  - Filterfelder und Statusflags für Sortierungen
     */

    private final VisitScheduleController visitScheduleController;
    private final VisitorController visitorController;
    private final CompanyController companyController;
    private final Grid<VisitSchedule> scheduleGrid = new Grid<>(VisitSchedule.class, false);
    private final TextField filterField = new TextField();

    // Hält immer den aktuell angezeigten Stand des Grids (inkl. aktiver Filter/Sortierung)
    private List<VisitSchedule> currentItems = new ArrayList<>();

    // Toggle-Status für wichtige Besuche
    private boolean importantSorted = false;

    //endregion Felder--------------------------------------------------------------------------------------------------


    //region Konstruktor------------------------------------------------------------------------------------------------

    /*
     * Initialisiert die Hauptansicht:
     *  - Setzt grundlegende Layout-Eigenschaften
     *  - Lädt Header, Toolbar und Daten-Grid
     *  - Konfiguriert Favicon für die Seite
     *  - Führt initiales Laden der Besuchsdaten aus
     */

    // Hauptcontainer
    public MainView(VisitScheduleController visitScheduleController,
                    VisitorController visitorController,
                    CompanyController companyController) {
        this.visitScheduleController = visitScheduleController;
        this.visitorController = visitorController;
        this.companyController = companyController;

        // Tab Icon
        UI.getCurrent().getPage().executeJs(
                "let f=document.querySelector('link[rel=\"icon\"]')||document.createElement('link');" +
                        "f.rel='icon';f.href=$0;document.head.appendChild(f);",
                "/frontend/images/favicon.ico"
        );

        addClassName("main-view");
        setSizeFull();
        add(createHeader());
        add(createToolbar());
        configureGrid();
        add(scheduleGrid);
        updateList();
    }

    //endregion Konstruktor---------------------------------------------------------------------------------------------


    //region Header-----------------------------------------------------------------------------------------------------

    /*
     * Erstellt den Kopfbereich der Anwendung:
     *  - Enthält Navigationsbuttons für die Hauptmodule
     *  - Markiert die aktuelle Seite optisch als aktiv
     *  - Sorgt für konsistentes Layout in allen Ansichten
     */

    private HorizontalLayout createHeader() {

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);

        // Navigations-Buttons
        Button visitorOverview = new Button("Besuchsübersicht", e -> getUI().ifPresent(ui -> ui.navigate("")));
        visitorOverview.addClassName("nav-button-active");

        Button companies = new Button("Firmenübersicht", e -> getUI().ifPresent(ui -> ui.navigate("company")));
        companies.addClassName("nav-button");

        Button logoutButton = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClassName("logout-button");
        logoutButton.addClickListener(e -> UI.getCurrent().getPage().executeJs(
                "fetch('/logout', {method: 'POST'}).then(() => window.location.href='/loginview');"
        ));

        HorizontalLayout navButtons = new HorizontalLayout(visitorOverview, companies);
        navButtons.setSpacing(true);

        HorizontalLayout rightSide = new HorizontalLayout(logoutButton);
        rightSide.setAlignItems(Alignment.CENTER);

        header.add(navButtons, rightSide);
        return header;
    }

    //endregion Header--------------------------------------------------------------------------------------------------


    //region Toolbar----------------------------------------------------------------------------------------------------

    /*
     * Erstellt die obere Werkzeugleiste (Toolbar):
     *  - Enthält Suchfeld, Datumsfilter und Buttons zum Sortieren oder Hinzufügen von Besuchen
     *  - Steuert die Interaktion zwischen Filter, Grid und Benutzeraktionen
     */

    private HorizontalLayout createToolbar() {

        //region Filterfelder-------------------------------------------------------------------------------------------

        // Filterfeld (Besucher, Firma, Zweck...)
        filterField.setPlaceholder("Suche nach Besucher, Firma, Zweck...");
        filterField.setClearButtonVisible(true);
        filterField.addValueChangeListener(e -> applyFilter(e.getValue()));
        filterField.addClassName("filter-field");

        // DatePicker
        DatePicker dateFilter = new DatePicker();
        configureDatePicker(dateFilter);
        dateFilter.setPlaceholder("Datum filtern");
        dateFilter.addClassName("date-filter");
        dateFilter.addValueChangeListener(e -> {
            LocalDate selectedDate = e.getValue();

            if (selectedDate == null) {
                updateList();
                return;
            }

            List<VisitSchedule> filtered =
                    visitScheduleController.findAllVisitSchedules()
                            .stream()
                            .filter(v ->
                                    v.getVisitDate() != null &&
                                            v.getVisitDate().isEqual(selectedDate))
                            .toList();

            setGridItems(filtered);
        });

        //endregion Filterfelder----------------------------------------------------------------------------------------


        //region Buttons-Toolbar----------------------------------------------------------------------------------------

        // Wichtige Besuche Button
        Button importantSortButton = new Button("Wichtige Besuche filtern", new Icon(VaadinIcon.EXCLAMATION_CIRCLE));
        importantSortButton.addClassName("important-button-toolbar");
        importantSortButton.addClickListener(e -> toggleImportantSort(importantSortButton));

        // Neuer Besuch Button
        Button add = new Button("Neuer Besuch", new Icon(VaadinIcon.PLUS_CIRCLE));
        add.addClassName("add-button");
        add.addClickListener(e -> openCreateDialog());

        //endregion Buttons-Toolbar-------------------------------------------------------------------------------------


        //region Anordnung----------------------------------------------------------------------------------------------

        // Filter-Anordnung
        HorizontalLayout filtersLayout = new HorizontalLayout(filterField, dateFilter, importantSortButton);
        filtersLayout.addClassName("filters-layout");
        filtersLayout.setWidthFull();

        // Toolbar
        HorizontalLayout toolbar = new HorizontalLayout(filtersLayout, add);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        return toolbar;

        //endregion Anordnung-------------------------------------------------------------------------------------------
    }


    //region Toolbar Logik----------------------------------------------------------------------------------------------

    /*
     * Logische Methoden der Toolbar:
     *  - toggleImportantSort(): Sortiert die aktuell angezeigten Einträge – wichtige kommen nach oben
     *  - applyFilter(): Filtert Grid-Einträge nach Benutzer-, Firmen- oder Zwecknamen
     *  - configureDatePicker(): Passt DatePicker-Komponente an deutsches Format an
     *  - setGridItems(): Zentrale Methode zum Setzen der Grid-Einträge und Aktualisieren von currentItems
     */

    /*
     * Wichtige Besuche Button Logik:
     * Sortiert ausschließlich die aktuell im Grid angezeigten Einträge (currentItems),
     * sodass aktive Datumsfilter oder Textsuchfilter erhalten bleiben.
     * Wichtige Einträge werden dabei an den Anfang der Liste verschoben.
     *
     * Wichtig: Die aktive Spaltensortierung des Grids wird beim Aktivieren zurückgesetzt,
     * da Vaadin diese nach setItems() erneut anwenden würde und so die gewünschte
     * Reihenfolge überschreiben würde.
     */
    private void toggleImportantSort(Button button) {
        if (!importantSorted) {
            List<VisitSchedule> sorted = new ArrayList<>(currentItems);
            sorted.sort((a, b) -> {
                if (a.isImportant() && !b.isImportant()) return -1;
                if (!a.isImportant() && b.isImportant()) return 1;
                return 0;
            });
            importantSorted = true;
            button.addClassName("important-button-toolbar-active");
            // Spaltensortierung zurücksetzen, damit Vaadin die eigene Sortierung
            // nicht über die wichtigen Einträge legt
            scheduleGrid.sort(Collections.emptyList());
            scheduleGrid.setItems(sorted);
        } else {
            importantSorted = false;
            button.removeClassName("important-button-toolbar-active");
            scheduleGrid.sort(Collections.emptyList());
            scheduleGrid.setItems(currentItems);
        }
    }

    // Filter Logik
    private void applyFilter(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            updateList();
            return;
        }
        String f = filter.trim().toLowerCase();
        List<VisitSchedule> filtered = visitScheduleController.findAllVisitSchedules().stream()
                .filter(s -> {
                    boolean matchesVisitor = s.getVisitor() != null && s.getVisitor().getName() != null
                            && s.getVisitor().getName().toLowerCase().contains(f);
                    boolean matchesCompany = (s.getCompanyName() != null
                            && s.getCompanyName().toLowerCase().contains(f))
                            || (s.getVisitor() != null && s.getVisitor().getCompany() != null
                            && s.getVisitor().getCompany().getCompanyName() != null
                            && s.getVisitor().getCompany().getCompanyName().toLowerCase().contains(f));
                    boolean matchesPurpose = s.getPurpose() != null && s.getPurpose().toLowerCase().contains(f);
                    return matchesVisitor || matchesCompany || matchesPurpose;
                }).collect(Collectors.toList());
        setGridItems(filtered);
    }

    /*
     * Zentrale Methode zum Setzen der Grid-Einträge:
     * Aktualisiert immer gleichzeitig currentItems und das Grid,
     * damit toggleImportantSort stets auf dem korrekten aktuellen Stand arbeitet.
     */
    private void setGridItems(List<VisitSchedule> items) {
        currentItems = new ArrayList<>(items);
        scheduleGrid.setItems(currentItems);
    }

    // DatePicker auf Deutsch anzeigen
    private void configureDatePicker(DatePicker datePicker) {
        datePicker.setLocale(Locale.GERMANY);
        datePicker.setI18n(new DatePicker.DatePickerI18n()
                .setToday("Heute")
                .setCancel("Abbrechen")
                .setDateFormat("dd.MM.yyyy")
                .setFirstDayOfWeek(1)
                .setMonthNames(List.of("Januar","Februar","März","April","Mai","Juni",
                        "Juli","August","September","Oktober","November","Dezember"))
                .setWeekdays(List.of("Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"))
                .setWeekdaysShort(List.of("So","Mo","Di","Mi","Do","Fr","Sa")));
    }

    //endregion Toolbar Logik-------------------------------------------------------------------------------------------

    //endregion Toolbar-------------------------------------------------------------------------------------------------


    //region Grid-Container---------------------------------------------------------------------------------------------

    /*
     * Konfiguriert das Haupt-Grid:
     *  - Erstellt Spalten für Besucher, Firma, Zweck, Datum, Uhrzeit und Aktionen
     *  - Setzt Renderer für farbliche Hervorhebungen und visuelle Markierungen
     *  - Legt Sortierung und Layoutverhalten fest
     */

    private void configureGrid() {

        //Grid-Container
        scheduleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        scheduleGrid.setSizeFull();
        scheduleGrid.addClassName("visit-grid");

        //region Zeilen/Spalten (Span-Klassen)--------------------------------------------------------------------------

        // Besucher-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(schedule -> {
            String name = schedule.getVisitor() != null ? schedule.getVisitor().getName() : "-";
            Span sp = new Span(name);
            sp.addClassName("visitor-col");
            if (schedule.isImportant()) sp.addClassName("important");
            return sp;
        })).setHeader("Besucher").setSortable(true).setComparator(s -> s.getVisitor() != null ?
                s.getVisitor().getName() : "").setAutoWidth(true);

        // Firma-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(schedule -> {
            String comp = "-";
            if (schedule.getVisitor() != null && schedule.getVisitor().getCompany() != null) {
                comp = schedule.getVisitor().getCompany().getCompanyName();
            } else if (schedule.getCompanyName() != null) {
                comp = schedule.getCompanyName();
            }
            Span sp = new Span(comp);
            sp.addClassName("company-col");
            if (schedule.isImportant()) sp.addClassName("important");
            return sp;
        })).setHeader("Firma").setSortable(true).setComparator(VisitSchedule::getCompanyName).setAutoWidth(true);

        // Zweck-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(schedule -> {
            String purpose = schedule.getPurpose();
            Span sp = new Span((purpose != null && !purpose.isBlank()) ? purpose : "-");
            sp.addClassName("purpose-col");
            if (schedule.isImportant()) sp.addClassName("important");
            return sp;
        })).setHeader("Zweck").setSortable(true).setAutoWidth(true);

        // Datum-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(schedule -> {
            LocalDate date = schedule.getVisitDate();
            Span sp = new Span(date != null ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "-");
            sp.addClassName("date-col");
            if (schedule.isImportant()) sp.addClassName("important");
            if (date != null && date.isEqual(LocalDate.now())) sp.addClassName("today");
            return sp;
        })).setHeader("Datum").setSortable(true).setComparator(VisitSchedule::getVisitDate).setAutoWidth(true);

        // Uhrzeit-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(schedule -> {
            String t = schedule.getArrivalTime() != null ? schedule.getArrivalTime().toString() : "-";
            Span sp = new Span(t);
            sp.addClassName("time-col");
            if (schedule.isImportant()) sp.addClassName("important");
            return sp;
        })).setHeader("Uhrzeit").setSortable(true).setComparator(VisitSchedule::getArrivalTime).setAutoWidth(true);

        // Aktionen-Spalte
        scheduleGrid.addColumn(new ComponentRenderer<>(this::createActionButtons))
                .setHeader("Aktionen").setAutoWidth(true);

        //endregion Zeilen/Spalten (Span-Klassen)-----------------------------------------------------------------------
    }

    //endregion Grid-Container------------------------------------------------------------------------------------------


    //region Action-Buttons---------------------------------------------------------------------------------------------

    /*
     * Erstellt Aktions-Buttons für jede Tabellenzeile:
     *  - Bearbeiten: Öffnet Dialog zur Änderung des Besuchs
     *  - Löschen: Entfernt den Besuchseintrag aus der Datenbank
     *  - Wichtig: Markiert Besuche als besonders relevant
     *
     * Zusätzlich:
     *  - Nach jeder Aktion wird eine kurze Benachrichtigung oben rechts angezeigt.
     *  - updateList(): Aktualisiert die angezeigten Daten im Grid
     */

    private Component createActionButtons(VisitSchedule schedule) {

        // Bearbeitungs-Button
        Button edit = new Button(new Icon(VaadinIcon.PENCIL));
        edit.addClassName("edit-button");
        edit.addClickListener(e -> openEditDialog(schedule));

        // Lösch-Button
        Button del = new Button(new Icon(VaadinIcon.TRASH));
        del.addClassName("delete-button");
        del.addClickListener(e -> {
            visitScheduleController.deleteScheduleById(schedule.getId());
            updateList();
            Notification.show("Besuch gelöscht", 2000, Notification.Position.TOP_END);
        });

        // Wichtige Besuche-Button
        Button importantButton = new Button(new Icon(VaadinIcon.EXCLAMATION_CIRCLE));
        importantButton.addClassName("important-button");
        if (schedule.isImportant()) importantButton.addClassName("active");
        importantButton.addClickListener(e -> {
            schedule.setImportant(!schedule.isImportant());
            visitScheduleController.saveSchedule(schedule);
            scheduleGrid.getDataProvider().refreshItem(schedule);
            importantButton.getClassNames().remove("active");
            if (schedule.isImportant()) importantButton.addClassName("active");
            Notification.show(schedule.isImportant() ? "Als wichtig markiert" : "Markierung entfernt", 1500,
                    Notification.Position.TOP_END);
        });

        // Action-Buttons
        HorizontalLayout actions = new HorizontalLayout(edit, del, importantButton);
        actions.addClassName("action-buttons");
        return actions;
    }

    /*
     * Lädt alle Einträge aus der Datenbank und zeigt sie im Grid an.
     * Setzt currentItems zurück auf den vollständigen, ungefilterten Stand.
     */
    private void updateList() {
        setGridItems(visitScheduleController.findAllVisitSchedules());
    }

    //endregion Action-Buttons------------------------------------------------------------------------------------------


    //region Neuer Besuch & Bearbeiten----------------------------------------------------------------------------------

    /*
     * Beinhaltet die Dialoglogik für:
     *  - Erstellen eines neuen Besuchseintrags
     *  - Bearbeiten bestehender Einträge
     *
     * Beide Dialoge verwenden konsistente Formulare, Validierungen und Benachrichtigungen.
     */

    //region Neuer Besuch-----------------------------------------------------------------------------------------------

    /*
     * Öffnet den Dialog zum Erstellen eines neuen Besuchs:
     *  - Bietet Eingabefelder für Besucher, Firma, Zweck, Datum und Uhrzeit
     *  - Ermöglicht Auswahl bestehender Datensätze oder Eingabe neuer Namen
     *  - Enthält Schaltflächen für Speichern, Abbrechen und „Speichern & Weiteren hinzufügen"
     */

    private void openCreateDialog() {

        //region Neuer Besuch Container---------------------------------------------------------------------------------

        // Neuer Besuch äußerer Container
        Dialog dialog = new Dialog();
        dialog.addClassName("create-dialog");

        // Neuer Besuch innerer Container
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("dialog-form");
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Header-Container für Neuer Besuch Formular
        Div headerContainer = new Div();
        headerContainer.addClassName("header-container");
        H2 header = new H2("Neuen Besuch anlegen");
        header.addClassName("header-title");
        headerContainer.add(header);

        //endregion Neuer Besuch Container------------------------------------------------------------------------------

        //region Neuer Besuch Eingabefelder-----------------------------------------------------------------------------

        // Existierenden Besucher auswählen Feld
        ComboBox<Visitor> visitorCombo = new ComboBox<>();
        visitorCombo.setPlaceholder("Existierenden Besucher wählen");
        visitorCombo.setItemLabelGenerator(v -> v.getName() + (v.getCompany() != null ? " (" + v.getCompany()
                .getCompanyName() + ")" : ""));
        visitorCombo.setItems(visitorController.getAllVisitors().stream().sorted(Comparator
                .comparing(Visitor::getName)).toList());

        // Neuer Besucher Feld
        TextField newVisitorName = new TextField();
        newVisitorName.setPlaceholder("Neuer Besucher");
        ComboBox<Company> companyCombo = new ComboBox<>();
        companyCombo.setPlaceholder("Firma wählen");
        companyCombo.setItemLabelGenerator(Company::getCompanyName);
        companyCombo.setItems(companyController.getAllCompanies().stream().sorted(Comparator
                .comparing(Company::getCompanyName)).toList());
        TextField newCompanyName = new TextField();
        newCompanyName.setPlaceholder("Neue Firma");

        visitorCombo.addValueChangeListener(e -> {
            boolean hasVisitor = e.getValue() != null;
            newVisitorName.setEnabled(!hasVisitor);
            companyCombo.setEnabled(!hasVisitor);
            newCompanyName.setEnabled(!hasVisitor);
        });

        // Zweck Feld
        TextField purpose = new TextField();
        purpose.setPlaceholder("Zweck");

        // Datum Felder
        DatePicker fromDate = new DatePicker();
        DatePicker toDate = new DatePicker();
        configureDatePicker(fromDate);
        configureDatePicker(toDate);
        fromDate.setPlaceholder("Von Datum");
        toDate.setPlaceholder("Bis Datum");
        fromDate.addValueChangeListener(e -> toDate.setMin(e.getValue()));

        // Uhrzeit Feld
        TimePicker time = new TimePicker();
        time.setPlaceholder("Uhrzeit");

        //endregion Neuer Besuch Eingabefelder--------------------------------------------------------------------------


        //region Neuer Besuch anlegen Logik-----------------------------------------------------------------------------

        /*
         * Enthält die Logik zum Speichern eines neuen Besuchs:
         *  - Verarbeitet Eingaben aus dem Dialog
         *  - Legt bei Bedarf neue Besucher oder Firmen an
         *  - Erstellt den Besuchseintrag und speichert ihn in der Datenbank
         *  - Aktualisiert anschließend das Grid
         *  - Zeigt eine kurze Bestätigungsnachricht an
         */

        Runnable saveVisit = () -> {
            Visitor chosenVisitor = visitorCombo.getValue();
            String newVisitor = newVisitorName.getValue() != null ? newVisitorName.getValue().trim() : "";
            Company chosenCompany = companyCombo.getValue();
            String newCompany = newCompanyName.getValue() != null ? newCompanyName.getValue().trim() : "";

            // Firma prüfen oder neu anlegen
            Company companyToUse = chosenCompany;
            if (companyToUse == null && !newCompany.isEmpty()) {
                Company c = new Company();
                c.setCompanyName(newCompany);
                companyToUse = companyController.saveCompany(c);
            }

            // Besucher prüfen oder neu anlegen
            Visitor visitorToUse = chosenVisitor;
            if (visitorToUse == null && !newVisitor.isEmpty()) {
                Visitor v = new Visitor();
                v.setName(newVisitor);
                v.setCompany(companyToUse);
                visitorToUse = visitorController.saveVisitor(v);
            }

            if (fromDate.getValue() == null) {
                Notification.show("Startdatum ist erforderlich", 2000, Notification.Position.TOP_END);
                return;
            }

            LocalDate start = fromDate.getValue();
            LocalDate end = toDate.getValue() != null ? toDate.getValue() : start;

            if (end.isBefore(start)) {
                Notification.show("Bis-Datum darf nicht vor dem Von-Datum liegen", 2000,
                        Notification.Position.TOP_END);
                return;
            }

            // Besuchseintrag erstellen und befüllen
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                VisitSchedule schedule = new VisitSchedule();
                schedule.setVisitor(visitorToUse);
                schedule.setCompanyName(companyToUse != null ? companyToUse.getCompanyName() :
                        (chosenVisitor != null &&
                                chosenVisitor.getCompany() != null ?
                                chosenVisitor.getCompany().getCompanyName() : null));
                schedule.setPurpose(purpose.getValue());
                schedule.setVisitDate(d);
                schedule.setArrivalTime(time.getValue());
                visitScheduleController.saveSchedule(schedule);
            }

            updateList();
            Notification.show("Besuch angelegt", 2000, Notification.Position.TOP_END);
        };

        //endregion Neuer Besuch anlegen Logik--------------------------------------------------------------------------

        //region Neuer Besuch Buttons-----------------------------------------------------------------------------------

        // Speichern Button
        Button saveClose = new Button("Speichern", e -> { saveVisit.run(); dialog.close();});
        saveClose.addClassName("save-button");

        // Abbrechen Button
        Button cancel = new Button("Abbrechen", e -> dialog.close());
        cancel.addClassName("cancel-button");

        // Speichern & Weiteren Besuch hinzufügen Button
        Button saveNew = new Button("Speichern & Weiteren hinzufügen", e -> {
            saveVisit.run();
            visitorCombo.clear();
            newVisitorName.clear();
            companyCombo.clear();
            newCompanyName.clear();
            purpose.clear();
            fromDate.clear();
            toDate.clear();
            time.clear();
            visitorCombo.focus();
        });
        saveNew.addClassName("save-button-secondary");

        HorizontalLayout buttons = new HorizontalLayout(saveClose, cancel, saveNew);
        buttons.addClassName("dialog-buttons");

        formLayout.add(headerContainer, visitorCombo, newVisitorName, companyCombo, newCompanyName, purpose, fromDate,
                toDate, time, buttons);
        dialog.add(formLayout);
        dialog.open();

        //endregion Neuer Besuch Buttons--------------------------------------------------------------------------------
    }

    //endregion Neuer Besuch--------------------------------------------------------------------------------------------

    //region Besucher bearbeiten----------------------------------------------------------------------------------------

    /*
     * Öffnet den Dialog zum Bearbeiten eines bestehenden Besuchs:
     *  - Lädt aktuelle Werte des Besuchs in die Formularfelder
     *  - Ermöglicht Änderungen an Name, Firma, Zweck, Datum und Uhrzeit
     *  - Aktualisiert den Datensatz nach dem Speichern in der Datenbank
     *  - Zeigt nach erfolgreicher Bearbeitung eine Bestätigungsmeldung
     */

    private void openEditDialog(VisitSchedule schedule) {

        //region Besuch bearbeiten Container----------------------------------------------------------------------------

        // Besuch bearbeiten äußerer Container
        Dialog dialog = new Dialog();
        dialog.addClassName("edit-dialog");

        // Besuch bearbeiten innerer Container
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("dialog-form-edit");
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Header-Container für Besuch bearbeiten Formular
        Div headerContainer = new Div();
        headerContainer.addClassName("header-container-edit");
        H2 header = new H2("Besuch bearbeiten");
        header.addClassName("header-title-edit");
        headerContainer.add(header);

        //endregion Besuch bearbeiten Container-------------------------------------------------------------------------


        //region Besuch bearbeiten Eingabefelder------------------------------------------------------------------------

        // Aktuelle Werte übernehmen
        Visitor currentVisitor = schedule.getVisitor();
        String currentVisitorName = currentVisitor != null ? currentVisitor.getName() : "";
        String currentCompanyName = currentVisitor != null && currentVisitor.getCompany() != null
                ? currentVisitor.getCompany().getCompanyName()
                : (schedule.getCompanyName() != null ? schedule.getCompanyName() : "");

        // Eingabefelder
        TextField visitorNameField = new TextField("Besucher", currentVisitorName);
        TextField companyNameField = new TextField("Firma", currentCompanyName);
        TextField purposeField = new TextField("Zweck", schedule.getPurpose() != null ?
                schedule.getPurpose() : "");
        DatePicker fromDate = new DatePicker("Von Datum", schedule.getVisitDate());
        DatePicker toDate = new DatePicker("Bis Datum", schedule.getVisitDate());
        configureDatePicker(fromDate);
        configureDatePicker(toDate);
        fromDate.addValueChangeListener(e -> toDate.setMin(e.getValue()));
        TimePicker timeField = new TimePicker("Uhrzeit", schedule.getArrivalTime());

        // Buttons
        Button save = new Button("Speichern");
        save.addClassName("save-button");
        save.addClickListener(e -> {
            if (fromDate.getValue() == null) {
                Notification.show("Startdatum ist erforderlich", 2000, Notification.Position.TOP_END);
                return;
            }

            LocalDate start = fromDate.getValue();
            LocalDate end = toDate.getValue() != null ? toDate.getValue() : start;
            if (end.isBefore(start)) {
                Notification.show("Bis-Datum darf nicht vor dem Von-Datum liegen", 2000,
                        Notification.Position.TOP_END);
                return;
            }

            // Firma prüfen oder neu anlegen
            Company company = null;
            if (!companyNameField.getValue().isBlank()) {
                if (currentVisitor != null && currentVisitor.getCompany() != null) {
                    company = currentVisitor.getCompany();
                    company.setCompanyName(companyNameField.getValue().trim());
                } else {
                    company = new Company();
                    company.setCompanyName(companyNameField.getValue().trim());
                }
                company = companyController.saveCompany(company);
            }

            // Besucher prüfen oder neu anlegen
            Visitor visitor = currentVisitor != null ? currentVisitor : null;
            if (!visitorNameField.getValue().isBlank()) {
                if (visitor == null) {
                    visitor = new Visitor();
                }
                visitor.setName(visitorNameField.getValue().trim());
                if (company != null) visitor.setCompany(company);
                visitor = visitorController.saveVisitor(visitor);
            }

            // Bestehende Mehrtage-Einträge dieses Besuchers löschen (verhindert Duplikate)
            if (visitor != null && visitor.getId() != null) {
                List<VisitSchedule> existing = visitScheduleController.findByVisitor(visitor);
                for (VisitSchedule vs : existing) {
                    if (schedule.getId() == null || !vs.getId().equals(schedule.getId())) {
                        visitScheduleController.deleteScheduleById(vs.getId());
                    }
                }
            }

            // Bestehenden Eintrag aktualisieren
            if (visitor != null) schedule.setVisitor(visitor);
            if (company != null) schedule.setCompanyName(company.getCompanyName());
            schedule.setPurpose(purposeField.getValue());
            schedule.setArrivalTime(timeField.getValue());
            schedule.setVisitDate(start);
            visitScheduleController.saveSchedule(schedule);

            // Zusätzliche Tage bei von-bis
            for (LocalDate d = start.plusDays(1); !d.isAfter(end); d = d.plusDays(1)) {
                VisitSchedule s = new VisitSchedule();
                s.setVisitor(schedule.getVisitor());
                s.setCompanyName(schedule.getCompanyName());
                s.setPurpose(schedule.getPurpose());
                s.setVisitDate(d);
                s.setArrivalTime(schedule.getArrivalTime());
                visitScheduleController.saveSchedule(s);
            }

            updateList();
            dialog.close();
            Notification.show("Besuch aktualisiert", 2000, Notification.Position.TOP_END);
        });

        //endregion Besuch bearbeiten Eingabefelder---------------------------------------------------------------------


        //region Besuch bearbeiten Buttons------------------------------------------------------------------------------

        // Buttons
        Button cancel = new Button("Abbrechen", e -> dialog.close());
        cancel.addClassName("cancel-button");

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        buttons.addClassName("dialog-buttons-edit");

        formLayout.add(headerContainer, visitorNameField, companyNameField, purposeField, fromDate, toDate,
                timeField, buttons);
        dialog.add(formLayout);
        dialog.open();

        //endregion Besuch bearbeiten Buttons---------------------------------------------------------------------------
    }

    //endregion Besucher bearbeiten-------------------------------------------------------------------------------------

    //endregion Neuer Besuch & Bearbeiten-------------------------------------------------------------------------------
}

//endregion MainView----------------------------------------------------------------------------------------------------