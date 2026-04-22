package com.sh.evd.entrancevisitordisplaynew.entity;

//region Imports--------------------------------------------------------------------------------------------------------

/* Entity zur Modellierung einer Rolle in der Anwendung.
 *  - Enthält Name, Beschreibung und Typ der Rolle
 *  - Verknüpfung zu Usern über Many-to-Many Beziehung
 */

import com.sh.evd.entrancevisitordisplaynew.entity.BaseEntity;
import com.sh.evd.entrancevisitordisplaynew.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region RoleEntity-----------------------------------------------------------------------------------------------------

/* Role-Entity für die Persistenzschicht.
 *  - Basis: BaseEntity
 *  - Lombok für Getter und Setter
 */

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    // Name der Rolle (eindeutig)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Beschreibung der Rolle (optional)
    @Column(name = "description", columnDefinition = "text")
    private String description;

    // Typ der Rolle (Enum)
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 20)
    private RoleType roleType;

    // Benutzer, die dieser Rolle zugeordnet sind (Many-to-Many)
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}

//endregion RoleEntity--------------------------------------------------------------------------------------------------
