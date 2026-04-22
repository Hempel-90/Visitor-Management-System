package com.sh.evd.entrancevisitordisplaynew.service;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Enthält alle benötigten Imports für den VisitorService:
 *  - Visitor- und Company-Entity für Geschäftslogik
 *  - VisitRepository für CRUD-Operationen auf Besucher
 *  - VisitScheduleRepository für Zugriff auf Besuchseinträge
 *  - Optional für sichere Rückgaben einzelner Besucher
 *  - Spring Service Annotation zur Registrierung als Service-Komponente
 *  - List für Rückgabewerte von mehreren Besuchern
 */

import com.sh.evd.entrancevisitordisplaynew.entity.Visitor;
import com.sh.evd.entrancevisitordisplaynew.entity.VisitSchedule;
import com.sh.evd.entrancevisitordisplaynew.repository.VisitRepository;
import com.sh.evd.entrancevisitordisplaynew.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region VisitorService------------------------------------------------------------------------------------------------

/*
 * Visitor Service
 *
 * Verantwortlich für die Geschäftslogik rund um Besucher:
 *  - CRUD-Operationen auf Visitor-Entitäten
 *  - Abfragen nach Name, Company oder ID
 *  - Zählen von Besuchern nach Name oder Name + Company
 *  - Kapselt die Repository-Zugriffe
 *
 * Technologien:
 *  - Spring Service für Service-Layer-Komponente
 *  - Nutzung von VisitRepository und VisitScheduleRepository für Datenbankzugriff
 */

@Service
public class VisitorService {

    private final VisitRepository visitRepository;
    private final VisitScheduleRepository visitScheduleRepository;

    public VisitorService(VisitRepository visitRepository, VisitScheduleRepository visitScheduleRepository) {
        this.visitRepository = visitRepository;
        this.visitScheduleRepository = visitScheduleRepository;
    }

    public List<Visitor> getAllVisitors(){
        return visitRepository.findAll();
    }

    public Optional<Visitor> getVisitorById(Long id){
        return visitRepository.findById(id);
    }

    public List<Visitor> getVisitorsByName(String name){
        return visitRepository.findByName(name);
    }

    public Visitor saveVisitor(Visitor visitor){
        return visitRepository.save(visitor);
    }

    public void deleteVisitorById(Long id){
        visitRepository.deleteById(id);
    }

    public List<VisitSchedule> getVisitSchedulesForVisitor(Visitor visitor) {
        return visitScheduleRepository.findByVisitor(visitor);
    }

}

//endregion VisitorService---------------------------------------------------------------------------------------------
