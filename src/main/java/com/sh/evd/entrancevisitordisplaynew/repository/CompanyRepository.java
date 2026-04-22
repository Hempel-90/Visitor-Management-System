package com.sh.evd.entrancevisitordisplaynew.repository;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für das CompanyRepository:
 *  - JPA Repository für Standard-Datenbankoperationen
 *  - Entity-Klassen Company und Visitor
 *  - Spring Repository Annotation für die Bean-Registrierung
 *  - Java List für Rückgabewerte
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region CompanyRepository----------------------------------------------------------------------------------------------

/*
 * Company Repository
 *
 * Schnittstelle für CRUD-Operationen auf Company-Entitäten:
 *  - Erweiterung von JpaRepository bietet Standardmethoden wie save, findAll, findById, delete
 *  - Eigene Methode findByCompanyName zur Suche nach Firmennamen
 *
 * Technologien:
 *  - Spring Data JPA für Datenbankzugriff
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findByCompanyName(String companyName);

}

//endregion CompanyRepository-------------------------------------------------------------------------------------------
