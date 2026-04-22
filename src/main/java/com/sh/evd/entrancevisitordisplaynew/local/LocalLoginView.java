package com.sh.evd.entrancevisitordisplaynew.local;

//region Imports--------------------------------------------------------------------------------------------------------

/* Lokale Login-View für Entwicklungsumgebung.
 *  - Zeigt LoginForm und Beispielbenutzer aus SampleUsers an
 *  - Unterstützt Dev Mode Menu Hinweis
 *  - Prüft Authentifizierung vor Betreten der Seite
 */

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region LocalLoginView-------------------------------------------------------------------------------------------------

/* Login-Seite für lokale Entwicklungsumgebung.
 *  - Zeigt LoginForm für Authentifizierung
 *  - Listet Beispielbenutzer auf
 *  - Unterstützt einfache Dev-Hinweise
 */

@PageTitle("Local Login")
@AnonymousAllowed
@CssImport("./themes/local-login.css")
@Route(LocalLoginView.LOGIN_PATH)
class LocalLoginView extends Main implements BeforeEnterObserver {

    // Pfad für diese Login-Seite
    static final String LOGIN_PATH = "/local-login";

    // AuthenticationContext für aktuelle Authentifizierung prüfen
    private final AuthenticationContext authenticationContext;

    // LoginForm-Komponente
    private final LoginForm login;

    // Konstruktor
    LocalLoginView(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;

        // LoginForm initialisieren
        login = new LoginForm();
        login.setAction(LOGIN_PATH);
        login.setForgotPasswordButtonVisible(false);

        initialize();
    }

    //region Initialize-------------------------------------------------------------------------------------------------

    // Initialisiert Layout und Beispielbenutzer
    public void initialize() {
        var userList = new UnorderedList();
        SampleUsers.ALL_USERS.forEach(user -> userList
                .add(new ListItem(user.getAppUser().getPreferredUsername())));

        setSizeFull();

        var exampleUsers = new Div(
                new H3("Example users"),
                new Paragraph("The password for every user is: " + SampleUsers.SAMPLE_PASSWORD),
                userList
        );

        var centerDiv = new Div(login, exampleUsers);
        add(centerDiv);

        // Dev Mode Hinweis
        var devModeMenuDiv = new Div("You can also use the Dev Mode Menu here to impersonate any user!");
        devModeMenuDiv.addClassNames("dev-mode-speech-bubble");
        add(devModeMenuDiv);

        // Styling
        addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Background.CONTRAST_5
        );

        centerDiv.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL
        );

        exampleUsers.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.Padding.LARGE
        );
    }

    //endregion Initialize----------------------------------------------------------------------------------------------

    //region BeforeEnter------------------------------------------------------------------------------------------------

    // Prüft, ob Benutzer bereits authentifiziert ist oder ob Login-Fehler vorliegt
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticationContext.isAuthenticated()) {
            event.forwardTo("/");
            return;
        }

        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }

    //endregion BeforeEnter---------------------------------------------------------------------------------------------

}

//endregion LocalLoginView----------------------------------------------------------------------------------------------
