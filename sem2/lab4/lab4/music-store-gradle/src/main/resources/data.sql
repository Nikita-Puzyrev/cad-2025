INSERT INTO categories (name, description)
VALUES ('Гитары', 'Акустические и электрические гитары'),
    ('Клавишные', 'Синтезаторы и цифровые пианино');
INSERT INTO equipment (name, type, brand, price, quantity, category_id)
VALUES (
        'Fender Stratocaster',
        'Электрогитара',
        'Fender',
        1200.00,
        5,
        1
    ),
    (
        'Yamaha P-125',
        'Цифровое пианино',
        'Yamaha',
        800.00,
        3,
        2
    );