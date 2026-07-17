package com.sh.evd.entrancevisitordisplaynew.views;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die CompanyView:
 *  - Entity-Klasse „Company" für Firmenobjekte
 *  - Controller „CompanyController" für den Zugriff auf die Geschäftslogik
 *  - Vaadin-Komponenten für Layout, Buttons, Dialoge, Grid und Benachrichtigungen
 *  - Utility-Klassen für Streams und Filterlogik
 *  - Session-Verwaltung und Zugriffskontrolle (VaadinSession)
 */

import java.util.List;
import java.util.stream.Collectors;

import com.sh.evd.entrancevisitordisplaynew.controller.CompanyController;
import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region CompanyView----------------------------------------------------------------------------------------------------

/*
 * Die CompanyView stellt die Verwaltungsoberfläche für Firmen dar.
 *
 * Hauptfunktionen:
 *  - Anzeige aller Firmen in einer Tabelle (Grid)
 *  - Anlegen, Bearbeiten und Löschen von Firmendatensätzen
 *  - Filterung der Liste nach Firmennamen
 *  - Zugriffsbeschränkung auf eingeloggte Benutzer
 *
 * Technologien:
 *  - Vaadin Flow Framework für die UI-Implementierung
 *  - Controller „CompanyController" zur Datenverwaltung
 *  - CSS-Styling über „company-view.css"
 */
@PermitAll
@Route("company")
@PageTitle("Firmenübersicht")
@CssImport("./styles/company-view.css")

public class CompanyView extends VerticalLayout {

    //region Felder-----------------------------------------------------------------------------------------------------

    /*
     * Zentrale Controller- und UI-Komponenten:
     *  - companyController: Zugriff auf Firmendaten über den Controller-Layer
     *  - companyGrid: Tabellenansicht der Firmen
     *  - filterField: Eingabefeld für die Filterfunktion
     */

    private final CompanyController companyController;
    private final Grid<Company> companyGrid = new Grid<>(Company.class, false);
    private final TextField filterField = new TextField();

    //endregion Felder--------------------------------------------------------------------------------------------------


    //region Konstruktor------------------------------------------------------------------------------------------------

    /*
     * Initialisiert die Ansicht und lädt alle Firmen:
     *  - Setzt das Favicon
     *  - Erstellt Header, Toolbar und Grid
     *  - Lädt initial die Firmendaten
     *  - Wendet grundlegendes Styling an
     */

    public CompanyView(CompanyController companyController) {

        this.companyController = companyController;

        // Tab Icon
        com.vaadin.flow.component.UI.getCurrent().getPage().executeJs(
                "let f=document.querySelector('link[rel=\"icon\"]')||document.createElement('link');" +
                        "f.rel='icon';f.href=$0;document.head.appendChild(f);",
                "/frontend/images/favicon.ico"
        );

        addClassName("company-view");
        setSizeFull();

        add(createHeader());
        add(createToolbar());
        configureGrid();
        add(companyGrid);
        updateList();
    }

    //endregion Konstruktor---------------------------------------------------------------------------------------------


    //region Zugriffskontrolle------------------------------------------------------------------------------------------

    /*
     * Führt beim Aufruf der View eine Zugriffskontrolle durch:
     *  - Prüft, ob in der aktuellen Session ein Benutzer hinterlegt ist
     *  - Wenn kein Benutzer vorhanden ist, erfolgt eine Weiterleitung zur Login-Seite
     */

//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        super.onAttach(attachEvent);
//        Object user = VaadinSession.getCurrent().getAttribute("user");
//        if (user == null) {
//            UI.getCurrent().navigate(""); /* Weiterleitung zur Login-Seite */
//        }
//    }

    //endregion Zugriffskontrolle---------------------------------------------------------------------------------------


    //region Header-----------------------------------------------------------------------------------------------------

    /*
     * Erstellt den Kopfbereich der Anwendung:
     *  - Enthält Navigationsbuttons für die Hauptmodule
     *  - Markiert die aktuelle Seite optisch als aktiv
     *  - Sorgt für konsistentes Layout in allen Ansichten
     */

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("company-header");
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);

        // Navigations-Buttons
        Button visitorOverview = new Button("Besuchsübersicht", e ->
                getUI().ifPresent(ui -> ui.navigate("mainview")));
        visitorOverview.addClassName("nav-button");

        Button companies = new Button("Firmenübersicht", e ->
                getUI().ifPresent(ui -> ui.navigate("company")));
        companies.addClassName("nav-button-active");

        /* Button calender = new Button("Kalender", e ->
                getUI().ifPresent(ui -> ui.navigate("calendar"))); //für zukünftige Kalenderübersicht
        calender.addClassName("nav-button"); */

        Button logoutButton = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClassName("logout-button-company");

        logoutButton.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs(
                    "fetch('/logout', {method: 'POST'}).then(() => window.location.href='/loginview');"
            );
        });

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
     *  - Beinhaltet das Suchfeld für Firmen
     *  - Enthält Button zum Anlegen neuer Firmen
     *  - Steuert Layout und Filterverhalten der Firmenliste
     */

    private HorizontalLayout createToolbar() {

        //region Filterfeld---------------------------------------------------------------------------------------------

        filterField.setPlaceholder("Suche nach Firma...");
        filterField.setClearButtonVisible(true);
        filterField.addValueChangeListener(e -> applyFilter(e.getValue()));
        filterField.addClassName("filter-field-company");

        //endregion Filterfeld------------------------------------------------------------------------------------------


        //region Neue Firma Button--------------------------------------------------------------------------------------

        Button add = new Button("Neue Firma", new Icon(VaadinIcon.PLUS_CIRCLE));
        add.addClassName("add-button");
        add.addClickListener(e -> openCreateDialog());

        //endregion Neue Firma Button-----------------------------------------------------------------------------------


        //region Anordnung----------------------------------------------------------------------------------------------

        HorizontalLayout filtersLayout = new HorizontalLayout(filterField);
        filtersLayout.addClassName("filters-layout");
        filtersLayout.setWidthFull();

        HorizontalLayout toolbar = new HorizontalLayout(filtersLayout, add);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        return toolbar;

        //endregion Anordnung-------------------------------------------------------------------------------------------
    }


    //region Toolbar Filter Logik---------------------------------------------------------------------------------------

    /*
     * Implementiert die Filterlogik der Toolbar:
     *  - Filtert Firmen nach Name (Groß-/Kleinschreibung ignoriert)
     *  - Wenn kein Filtertext eingegeben ist, werden alle Firmen angezeigt
     */

    // Filter Logik
    private void applyFilter(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            updateList();
            return;
        }
        String f = filter.trim().toLowerCase();
        List<Company> filtered = companyController.getAllCompanies().stream()
                .filter(c -> c.getCompanyName() != null && c.getCompanyName().toLowerCase().contains(f))
                .collect(Collectors.toList());
        companyGrid.setItems(filtered);
    }

    //endregion Toolbar Filter Logik------------------------------------------------------------------------------------

    //endregion Toolbar-------------------------------------------------------------------------------------------------


    //region Grid-------------------------------------------------------------------------------------------------------

    /*
     * Konfiguriert das Firmen-Grid:
     *  - Definiert Spalten für Firmenname und Aktionen
     *  - Aktiviert visuelle Zeilenstreifen
     *  - Fügt Renderer für Formatierung hinzu
     *  - Ermöglicht Spaltensortierung und Größenanpassung
     */

    private void configureGrid() {

        //Grid-Container
        companyGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        companyGrid.setSizeFull();
        companyGrid.addClassName("company-grid");


        //region Zeilen/Spalten (Span-Klassen)--------------------------------------------------------------------------

        // Firmen Spalte
        companyGrid.addColumn(new ComponentRenderer<>(company -> {
                    Span sp = new Span(company.getCompanyName() != null ? company.getCompanyName() : "");
                    sp.addClassName("company-col");
                    return sp;
                }))
                .setHeader("Firmenname")
                .setSortable(true)
                .setComparator(c -> c.getCompanyName() != null ? c.getCompanyName().toLowerCase() : "")
                .setAutoWidth(true);

        // Aktionen Spalte
        companyGrid.addColumn(new ComponentRenderer<>(this::createActionButtons))
                .setHeader("Aktionen")
                .setAutoWidth(true);

        companyGrid.getColumns().forEach(col -> col.setResizable(true));

        //endregion Zeilen/Spalten (Span-Klassen)-----------------------------------------------------------------------
    }

    //endregion Grid----------------------------------------------------------------------------------------------------


    //region Action-Buttons---------------------------------------------------------------------------------------------

    /*
     * Erstellt die Aktionsbuttons für jede Firmenzeile:
     *  - Bearbeiten: Öffnet den Bearbeitungsdialog
     *  - Löschen: Entfernt den Firmeneintrag aus der Datenbank
     *  - updateList(): Aktualisiert die Anzeige nach Änderungen
     *
     * Zusätzlich:
     *  - Nach jeder Aktion wird eine kurze Benachrichtigung oben rechts angezeigt.
     *  - updateList(): Aktualisiert die angezeigten Daten im Grid
     */

    private Component createActionButtons(Company company) {

        // Bearbeitungs-Button
        Button edit = new Button(new Icon(VaadinIcon.PENCIL));
        edit.addClassName("edit-button");
        edit.addClickListener(e -> openEditDialog(company));

        // Lösch-Button
        Button delete = new Button(new Icon(VaadinIcon.TRASH));
        delete.addClassName("delete-button");
        delete.addClickListener(e -> {
            companyController.deleteCompany(company.getId());
            updateList();

            // Benachrichtigungsbox oben rechts
            Notification.show("Firma gelöscht", 2000, Notification.Position.TOP_END);
        });

        HorizontalLayout actions = new HorizontalLayout(edit, delete);
        actions.addClassName("action-buttons-company");
        return actions;
    }

    private void updateList() {
        companyGrid.setItems(companyController.getAllCompanies());
    }

    //endregion Action-Buttons------------------------------------------------------------------------------------------


    //region neue Firma und Firma bearbeiten----------------------------------------------------------------------------

    /*
     * Beinhaltet die Dialoglogik für:
     *  - Anlegen einer neuen Firma
     *  - Bearbeiten bestehender Firmen
     *
     * Beide Dialoge:
     *  - Nutzen ein einheitliches Formularlayout
     *  - Enthalten Validierungen und visuelles Feedback
     *  - Zeigen Benachrichtigungen nach jeder Aktion
     */


    //region Neue Firma-------------------------------------------------------------------------------------------------

    /*
     * Öffnet den Dialog zum Anlegen einer neuen Firma:
     *  - Bietet Eingabefeld für den Firmennamen
     *  - Validiert Eingaben
     *  - Enthält Buttons zum Speichern, Abbrechen und "Speichern & Weitere hinzufügen"
     *  - Nach dem Speichern wird das Grid aktualisiert und eine Bestätigung angezeigt
     */

    private void openCreateDialog() {

        //region Neue Firma Container-----------------------------------------------------------------------------------

        // Neue Firma äußerer Container
        Dialog dialog = new Dialog();
        dialog.addClassName("create-dialog");

        // Neue Firma innerer Container
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("dialog-form");
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Header-Container für Neue Firma Formular
        Div headerContainer = new Div();
        headerContainer.addClassName("header-container");
        H2 header = new H2("Neue Firma anlegen");
        header.addClassName("header-title");
        headerContainer.add(header);

        //endregion Neue Firma Container--------------------------------------------------------------------------------


        //region Neue Firma Eingabefelder-------------------------------------------------------------------------------

        // Neue Firma Feld
        TextField newCompanyName = new TextField();
        newCompanyName.setPlaceholder("Neue Firma");

        //endregion Neue Firma Eingabefelder----------------------------------------------------------------------------


        //region Speicher Logik-----------------------------------------------------------------------------------------

        /*
         * Logik zum Speichern einer neuen Firma:
         *  - Übernimmt Eingabewert aus dem Textfeld
         *  - Legt bei gültiger Eingabe eine neue Firma an
         *  - Aktualisiert die Liste im Grid
         *  - Zeigt bei Erfolg oder fehlender Eingabe eine Benachrichtigung an
         */

        Runnable saveCompany = () -> {
            String newCompany = newCompanyName.getValue() != null ? newCompanyName.getValue().trim() : "";
            if (!newCompany.isEmpty()) {
                Company c = new Company();
                c.setCompanyName(newCompany);
                companyController.saveCompany(c);
                updateList();
                // Benachrichtigungsbox oben rechts
                Notification.show("Firma angelegt", 2000, Notification.Position.TOP_END);
            } else {
                // Benachrichtigungsbox oben rechts
                Notification.show("Bitte einen Firmennamen eingeben", 2000, Notification.Position.TOP_END);
            }
        };

        //endregion Speicher Logik--------------------------------------------------------------------------------------


        //region Neue Firma Buttons-------------------------------------------------------------------------------------

        // Speichern Button
        Button saveClose = new Button("Speichern", e -> {
            saveCompany.run();
            dialog.close();
        });
        saveClose.addClassName("save-button");

        // Abbrechen Button
        Button cancel = new Button("Abbrechen", e -> dialog.close());
        cancel.addClassName("cancel-button");

        // Speichern & Weitere hinzufügen Button
        Button saveNew = new Button("Speichern & Weitere hinzufügen", e -> {
            saveCompany.run();
            newCompanyName.clear();
            newCompanyName.focus();
        });
        saveNew.addClassName("save-button-secondary-company");

        // Buttons anordnen
        HorizontalLayout buttons = new HorizontalLayout(saveClose, cancel);
        buttons.addClassName("dialog-buttons");

        // Zweite Zeile mit dem "Weitere hinzufügen"-Button
        VerticalLayout buttonLayout = new VerticalLayout(buttons, saveNew);
        buttonLayout.setPadding(false);
        buttonLayout.setSpacing(false);

        formLayout.add(headerContainer, newCompanyName, buttonLayout);
        dialog.add(formLayout);
        dialog.open();

        //endregion Neue Firma Buttons----------------------------------------------------------------------------------
    }

    //endregion Neue Firma----------------------------------------------------------------------------------------------


    //region Firma bearbeiten-------------------------------------------------------------------------------------------

    /*
     * Öffnet den Dialog zum Bearbeiten einer bestehenden Firma:
     *  - Zeigt den aktuellen Firmennamen im Eingabefeld
     *  - Erlaubt Änderungen und speichert sie in der Datenbank
     *  - Aktualisiert das Grid nach dem Speichern
     *  - Zeigt eine kurze Benachrichtigung mit Erfolg oder Fehlermeldung an
     */

    private void openEditDialog(Company company) {

        //region Firma bearbeiten Container-----------------------------------------------------------------------------

        // Firma bearbeiten äußerer Container
        Dialog dialog = new Dialog();
        dialog.addClassName("edit-dialog");

        // Firma bearbeiten innerer Container
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("dialog-form");
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Header-Container für Firma bearbeiten Formular
        Div headerContainer = new Div();
        headerContainer.addClassName("header-container-edit");
        H2 header = new H2("Firma bearbeiten");
        header.addClassName("header-title-edit");
        headerContainer.add(header);

        //endregion Firma bearbeiten Container--------------------------------------------------------------------------


        //region Firma bearbeiten Eingabefelder-------------------------------------------------------------------------

        // Firma Feld
        TextField companyNameField = new TextField("Firmenname");
        companyNameField.addClassName("company-name-edit");
        companyNameField.setWidthFull();
        companyNameField.setValue(company.getCompanyName() != null ? company.getCompanyName() : "");
        companyNameField.focus();

        //endregion Firma bearbeiten Eingabefelder----------------------------------------------------------------------


        //region Firma bearbeiten Buttons-------------------------------------------------------------------------------

        // Speichern Button (mit Aktualisierung in DB)
        Button save = new Button("Speichern");
        save.addClassName("save-button");
        save.addClickListener(e -> {
            String newName = companyNameField.getValue() != null ? companyNameField.getValue().trim() : "";
            if (!newName.isEmpty()) {
                company.setCompanyName(newName);
                companyController.saveCompany(company);
                updateList();
                Notification.show("Firma aktualisiert", 2000, Notification.Position.TOP_END);
                dialog.close();
            } else {
                Notification.show
                        ("Bitte einen gültigen Firmennamen eingeben", 2000, Notification.Position.TOP_END);
            }
        });

        // Abbrechen Button
        Button cancel = new Button("Abbrechen");
        cancel.addClassName("cancel-button");
        cancel.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        buttons.addClassName("dialog-buttons");

        formLayout.add(headerContainer, companyNameField, buttons);
        dialog.add(formLayout);
        dialog.open();

        //endregion Firma bearbeiten Buttons----------------------------------------------------------------------------
    }

    //endregion Firma bearbeiten----------------------------------------------------------------------------------------

    //endregion neue Firma und Firma bearbeiten-------------------------------------------------------------------------
}

//endregion CompanyView-------------------------------------------------------------------------------------------------