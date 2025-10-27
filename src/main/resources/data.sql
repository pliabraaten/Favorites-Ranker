


-- Prepopulate the db tables for testing
-- USE ALL CAPS FOR TABLES FOR SQL STATEMENTS
INSERT INTO APP_USER (user_id, username) VALUES (1, 'Tim');

INSERT INTO FAVORITES_LIST (favorites_list_id, user_id, list_name, is_ranked) VALUES (1, 1, 'Favorite Foods', false);

INSERT INTO ITEM (item_id, favorites_list_id, item_name, position) VALUES (1, 1, 'pizza', 1);
INSERT INTO ITEM (item_id, favorites_list_id, item_name, position) VALUES (2, 1, 'corn', 2);
INSERT INTO ITEM (item_id, favorites_list_id, item_name, position) VALUES (3, 1, 'tortillas', 3);


