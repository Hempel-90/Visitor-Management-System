package com.sh.evd.entrancevisitordisplaynew.repository;

//region Imports--------------------------------------------------------------------------------------------------------

import com.sh.evd.entrancevisitordisplaynew.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region UserRepository-------------------------------------------------------------------------------------------------

/*
 * Repository für den Datenbankzugriff auf die User-Entität.
 * Stellt zur Verfügung:
 *  - CRUD-Operationen
 *  - Spezifikationsbasierte JPA-Abfragen
 *  - Benutzerdefinierte Queries zur erweiterten Datenabfrage
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.entraUserId = :entraUserId")
    Optional<User> findByEntraUserIdWithRoles(@Param("entraUserId") String entraUserId);

    Optional<User> findByFullName(String name);
}

//region UserRepository-------------------------------------------------------------------------------------------------