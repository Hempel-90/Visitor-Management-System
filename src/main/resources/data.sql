
-- Testdaten für EVD

-- Firmen
INSERT INTO company (company_name, active)
VALUES ('TestCompany', TRUE),
       ('SuperTech GmbH', TRUE);

-- Besucher
INSERT INTO visitor (name, active, company_id)
VALUES ('Max Mustermann', TRUE, 1),
       ('Anna Beispiel', TRUE, 2),
       ('Tom Besucher', TRUE, 1);

-- Besuchspläne
INSERT INTO visit_schedule (company_name, purpose, visit_date, arrival_time, visitor_id)
VALUES ('TestCompany', 'Besprechung mit HR', '2025-10-10', '09:30:00', 1),
       ('SuperTech GmbH', 'Produktvorstellung', '2025-10-11', '11:00:00', 2),
       ('TestCompany', 'Technikmeeting', '2025-10-12', '14:15:00', 3);
