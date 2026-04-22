-- Tabelle Company
CREATE TABLE company (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         company_name VARCHAR(255) NOT NULL,
                         active BOOLEAN NOT NULL
);

-- Tabelle Visitor
CREATE TABLE visitor (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         active BOOLEAN NOT NULL,
                         company_id BIGINT NOT NULL,
                         CONSTRAINT fk_visitor_company FOREIGN KEY (company_id) REFERENCES company(id)
);

-- Tabelle VisitSchedule
CREATE TABLE visit_schedule (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                company_name VARCHAR(255) NOT NULL,
                                purpose VARCHAR(255) NOT NULL,
                                visit_date DATE NOT NULL,
                                arrival_time TIME NOT NULL,
                                visitor_id BIGINT NOT NULL,
                                CONSTRAINT fk_schedule_visitor FOREIGN KEY (visitor_id) REFERENCES visitor(id)
);
