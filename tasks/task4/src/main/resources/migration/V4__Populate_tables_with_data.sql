INSERT INTO positions(name, salary)
VALUES ('уборщик', 1000),
       ('терапевт', 1500),
       ('старшая медсестра', 1250),
       ('хирург', 2000),
       ('офтальмолог', 1750);

INSERT INTO cabinets(name, number)
VALUES ('кладовая', '3-103'),
       ('кабинет процедурный', '4-103'),
       ('кабитнет терапевтический', '5-103'),
       ('кабинет офтальмологический', '2-103'),
       ('кабинет врача-хирурга', '1-101');

INSERT INTO appointment_types(name)
VALUES ('медкомиссия'),
       ('больничный'),
       ('первичный'),
       ('повторный'),
       ('контрольный');

INSERT INTO patients(fio, phone_number, address, birthday, passport)
VALUES ('Краснополов А.В.', '375255002070', 'г.Гомель, ул. Кожара д.110, кв.15', '2000-03-03', 'HB8372341'),
       ('Жмых Т.И.', NULL, 'г.Гомель, ул. Мазурова д.10, кв.51', '1992-01-15', 'HB0934275'),
       ('Персов Д.А.', '375299993030', 'г.Гомель, ул. Советская д.35, кв.82', '1950-09-12', 'HB4832345'),
       ('Кравченко В.С.', '375440981212', 'г.Гомель, ул. Головацкого д.102, кв.98', '1999-11-25', 'HB5472834'),
       ('Куркуленко Ж.Н.', '375337899889', NULL, '1975-02-19', 'PP9871235');

INSERT INTO staff(fio, phone_number, position_id, hire_date)
VALUES ('Желтухов Д.А.', NULL, 101, '1990-02-20'),
       ('Овчаренко О.Д.', '375296781234', 102, '2023-05-01'),
       ('Страшенко А.Г.', '375257892345', 103, '2024-09-25'),
       ('Бригадко П.А.', '375337589232', 104, '2024-01-11'),
       ('Мастабаев С.П,', '375449870123', 105, '2024-05-19');

INSERT INTO prescriptions(doctor_id, patient_id, description, issue_date, expiration_date)
VALUES (102, 101, 'Адицев 5 дней 3 раза в день по 1 таблетке', '2023-06-01', '2023-06-30'),
       (104, 102, 'Нимесулид пить 7 дней по 2 раза в день по 1 таблетке', '2023-10-01', '2023-11-30'),
       (105, 103, 'Тобрекс 7 дней капать по 2 раза в день', '2023-11-01', '2023-11-30'),
       (104, 104, 'Целебрекс 5 дней по 2 раза в день по 2 таблетки', '2024-02-01', '2024-02-29'),
       (102, 105, 'Азикар 7 дней по 3 раза в день по 2 таблетки', '2024-06-01', '2024-06-30');

INSERT INTO appointments(patient_id, doctor_id, cabinet_id, type_id, appointment_date)
VALUES (101, 102, 103, 103, '2024-06-03 12:45:00'),
       (102, 103, 102, 102, '2024-06-10 17:15:00'),
       (103, 104, 105, 101, '2024-06-11 08:00:00'),
       (104, 105, 104, 105, '2024-06-20 19:15:00'),
       (105, 103, 102, 104, '2024-06-25 11:30:00');
