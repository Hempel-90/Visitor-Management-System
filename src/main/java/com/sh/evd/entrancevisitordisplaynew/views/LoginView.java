package com.sh.evd.entrancevisitordisplaynew.views;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für die LoginView:
 *  - Vaadin-Komponenten für Layout, Buttons, Textfelder und Benachrichtigungen
 *  - CSS-Import für das Styling der Login-Ansicht
 *  - Routing- und Session-Verwaltung für die Benutzeranmeldung
 */

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region LoginView------------------------------------------------------------------------------------------------------

/*
 * Die LoginView stellt die Benutzeranmeldungsoberfläche dar.
 *
 * Hauptfunktionen:
 *  - Bereitstellung eines Login-Formulars mit Benutzername und Passwort
 *  - Überprüfung der Anmeldedaten und Authentifizierung
 *  - Weiterleitung nach erfolgreichem Login zur Hauptansicht
 *  - Anzeige einer Fehlermeldung bei ungültigen Anmeldedaten
 *
 * Technologien:
 *  - Vaadin Flow Framework für die UI-Implementierung
 *  - CSS-Styling über „login-view.css“
 */
@PermitAll
@Route("")
@PageTitle("Login")
@CssImport("./styles/login-view.css")

public class LoginView extends VerticalLayout {

    //region Konstruktor------------------------------------------------------------------------------------------------

    /*
     * Der Konstruktor der LoginView erstellt das gesamte Layout und UI-Elemente:
     *  - Initialisierung der verschiedenen Layouts und UI-Komponenten (Textfelder, Button)
     *  - Setzt das Layout für das Login-Formular zusammen
     *  - Verarbeitet die Login-Logik und zeigt eine Fehlermeldung bei ungültigen Eingaben
     */

    public LoginView() {

        //region Container----------------------------------------------------------------------------------------------

        /*
         * Container-Layouts, die für die Struktur und das Layout der Login-Seite verantwortlich sind:
         *  - Setzt das Hauptlayout auf 100% der Bildschirmgröße und zentriert die Elemente
         *  - Die Container werden in ein äußeres und ein inneres Layout unterteilt
         */

        // Haupt-Container

        // Tab Icon
        com.vaadin.flow.component.UI.getCurrent().getPage().executeJs(
                "let f=document.querySelector('link[rel=\"icon\"]')||document.createElement('link');" +
                        "f.rel='icon';f.href=$0;document.head.appendChild(f);",
                "/frontend/images/favicon.ico"
        );

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-view");

        // Login äußerer Container
        VerticalLayout loginOverViewContainer = new VerticalLayout();
        loginOverViewContainer.addClassName("login-over-view-container");
        loginOverViewContainer.setPadding(false);
        loginOverViewContainer.setSpacing(false);
        loginOverViewContainer.setAlignItems(Alignment.CENTER);
        loginOverViewContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        // Login innerer Container
        VerticalLayout loginViewContainer = new VerticalLayout();
        loginViewContainer.addClassName("login-view-container");
        loginViewContainer.setWidth(null);

        //endregion Container-------------------------------------------------------------------------------------------


        //region Login-Container-Inhalt---------------------------------------------------------------------------------

        /*
         * Definiert die UI-Elemente im Login-Formular:
         *  - Header (Titel des Formulars)
         *  - Eingabefelder für Benutzername und Passwort
         *  - Login-Button, der das Login auslöst
         */

        // Header Login-Container
        H2 loginHeader = new H2("Login");
        loginHeader.addClassName("login-header");

        // Login-Button
        Button loginButton = new Button("Anmelden",
                e -> UI.getCurrent().navigate(MainView.class));
        loginButton.addClassName("login-button");

        //endregion Login-Container-Inhalt------------------------------------------------------------------------------


        //region Test-Benutzer------------------------------------------------------------------------------------------

        /*
         * Verarbeitet die Login-Logik:
         *  - Überprüft, ob der Benutzername und das Passwort mit einem vordefinierten Testbenutzer übereinstimmen
         *  - Bei erfolgreicher Anmeldung wird der Benutzer zur Hauptansicht weitergeleitet
         *  - Bei ungültigen Anmeldedaten wird eine Fehlermeldung angezeigt
         */



        /* Fügt die Layouts zum Haupt-Layout hinzu */
        add(loginOverViewContainer);
        loginViewContainer.add(loginHeader, loginButton);
        add(loginViewContainer);

        //endregion Test-Benutzer---------------------------------------------------------------------------------------
    }

    //endregion Konstruktor---------------------------------------------------------------------------------------------

}


//endregion LoginView---------------------------------------------------------------------------------------------------

