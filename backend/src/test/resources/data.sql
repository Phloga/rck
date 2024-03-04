INSERT INTO item (IS_BASE_INGREDIENT,ID ,NAME ) VALUES
(TRUE,1,'Zucker'),
(TRUE,2,'Salz'),
(TRUE,3,'Milch'),
(TRUE,4,'Mehl'),
(TRUE,5,'Butter'),
(FALSE,6,'Salzige Milch'),
(FALSE,7,'Süße Milch'),
(FALSE,8,'Butter+');


INSERT INTO recipe (ID,NAME,CONTENT,INGREDIENT_COUNT) VALUES
(0, 'Salzige Milch', 'Salz in Milch geben', 2),
(1, 'Süße Milch', 'Zucker in Milch geben', 2),
(2, 'Butter+', 'Butter auf den Tisch legen und zum Molkerrei Gott beten', 1);


INSERT INTO unit (ID, NAME, SYMBOL, TYPE, BASE_CONVERSION_MULTIPLIER) VALUES
(0, 'Gram', 'g' , 'MASS', 1.0),
(1, 'Milliliter', 'ml' , 'VOLUME', 1.0);


INSERT INTO recipe_line (ITEM_ID, RECIPE_ID, UNIT_ID, AMOUNT, IS_OPTIONAL, IS_OUTPUT) VALUES
(3, 0, 1, 1000, FALSE, FALSE),
(2, 0, 0, 50, FALSE, FALSE),
(6, 0, 1, 1000, FALSE, TRUE),
(3, 1, 1, 1000, FALSE, FALSE),
(1, 1, 0, 50, FALSE, FALSE),
(7, 1, 1, 1000, FALSE, TRUE),
(5, 2, 1, 250, FALSE, FALSE),
(8, 2, 1, 250, FALSE, TRUE);