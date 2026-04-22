
//region Imports--------------------------------------------------------------------------------------------------------

import '../themes/LoginView.css';
import { FontAwesomeIcon } from '../utils/fontawesome';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

//endregion Imports-----------------------------------------------------------------------------------------------------

//region Login View Konfiguration---------------------------------------------------------------------------------------

/*
 * Login component page
 * Handles OIDC login redirection.
 */

const OIDC_LOGIN_URL = '/oauth2/authorization/azure'; // static URL für Server-Kompatibilität

export const config: ViewConfig = {
    menu: { exclude: true },
    loginRequired: false
};

//endregion Login View Konfiguration------------------------------------------------------------------------------------


//region Login Komponente-----------------------------------------------------------------------------------------------

export default function Login() {

    //region Login-Formular Layout--------------------------------------------------------------------------------------

    /*
     * Hauptcontainer:
     *  - Zentriert die Login-Box
     *  - Enthält Header und Login-Button
     */

    return (
        <main className="login-main">
            <div className="login-body">

                {/* Header */}
                <header className="login-header">
                    <div>
                        <FontAwesomeIcon icon="certificate" />
                    </div>
                    <p>Authentifizierung</p>
                    <span>Bitte drücken Sie "Anmelden", um sich zu authentifizieren.</span>
                </header>

                {/* Login Button */}
                <div className="login-form">
                    <a
                        className="loginLink"
                        href="#"
                        onClick={(e) => {
                            e.preventDefault();
                            window.location.href = OIDC_LOGIN_URL;
                        }}
                    >
                        Anmelden
                    </a>
                </div>

            </div>
        </main>
    );

    //endregion Login-Formular Layout----------------------------------------------------------------------------------
}

//endregion Login Komponente------------------------------------------------------------------------------------------
