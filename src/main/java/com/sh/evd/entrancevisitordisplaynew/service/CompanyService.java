package com.sh.evd.entrancevisitordisplaynew.service;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den CompanyService:
 *  - Company-Entity und CompanyRepository für CRUD-Operationen
 *  - Optional für sichere Rückgaben einzelner Firmen
 *  - Spring Service Annotation zur Registrierung als Service-Komponente
 *  - List für Rückgabewerte von mehreren Firmen
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Company;
import com.sh.evd.entrancevisitordisplaynew.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------


//region CompanyService------------------------------------------------------------------------------------------------

/*
 * Company Service
 *
 * Verantwortlich für die Geschäftslogik rund um Firmen:
 *  - CRUD-Operationen auf Company-Entitäten
 *  - Abfragen nach ID oder Name
 *  - Kapselt die Repository-Zugriffe
 *
 * Technologien:
 *  - Spring Service für Service-Layer-Komponente
 *  - Nutzung von CompanyRepository für Datenbankzugriff
 */

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Company> getCompaniesByName(String name) {
        return companyRepository.findByCompanyName(name);
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }
}

//endregion CompanyService--------------------------------------------------------------------------------------------
