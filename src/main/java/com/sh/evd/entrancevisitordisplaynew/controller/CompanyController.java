package com.sh.evd.entrancevisitordisplaynew.controller;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den CompanyController:
 *  - Entity-Klasse „Company" für Firmenobjekte
 *  - Service „CompanyService" für CRUD-Operationen auf Firmen
 *  - Spring Component-Annotation für die Bean-Verwaltung
 *  - Java-Utilities für Listenoperationen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.sh.evd.entrancevisitordisplaynew.service.CompanyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region CompanyController----------------------------------------------------------------------------------------------

/*
 * Der CompanyController koordiniert die Kommunikation zwischen der View- und der Service-Schicht.
 *
 * Hauptfunktionen:
 *  - Abrufen aller Firmen oder einzelner Firmen nach ID
 *  - Suche von Firmen nach Name
 *  - Anlegen, Aktualisieren und Löschen von Firmen
 *
 * Technologien:
 *  - Spring Component für die Bean-Verwaltung durch den Spring-Container
 *  - Delegation der Geschäftslogik an den CompanyService
 */

@Component
public class CompanyController {

    // Service für die Geschäftslogik rund um Firmen
    private final CompanyService companyService;

    // Konstruktor mit Dependency Injection für den CompanyService
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    //region Methoden---------------------------------------------------------------------------------------------------

    /*
     * Dieser Block enthält alle Methoden des CompanyControllers:
     *  - Alle Firmen oder einzelne Firmen nach ID abrufen
     *  - Firmen nach Name suchen
     *  - Firma erstellen oder aktualisieren
     *  - Firma löschen
     */

    // Alle Firmen abrufen
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    // Firma nach ID abrufen
    public Optional<Company> getCompanyById(Integer id) {
        return companyService.getCompanyById(id);
    }

    // Firmen nach Name suchen
    public List<Company> getCompaniesByName(String name) {
        return companyService.getCompaniesByName(name);
    }

    // Firma speichern oder aktualisieren
    public Company saveCompany(Company company) {
        return companyService.saveCompany(company);
    }

    // Firma löschen
    public void deleteCompany(Integer id) {
        companyService.deleteCompany(id);
    }

    //endregion Methoden------------------------------------------------------------------------------------------------
}

//endregion CompanyController-------------------------------------------------------------------------------------------