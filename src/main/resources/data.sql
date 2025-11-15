


-- Prepopulate the db tables for testing
-- USE ALL CAPS FOR TABLES FOR SQL STATEMENTS

--INSERT INTO USERS (username)  -- Populate table with Tim only if a Tim record doesn't already exist
--    SELECT 'Tim'
--    WHERE NOT EXISTS (SELECT * FROM USERS);
--
--INSERT INTO FAVORITES_LIST (user_id, list_name, is_ranked)
--    SELECT 1, 'Favorite Foods', false
--    WHERE NOT EXISTS (SELECT * FROM FAVORITES_LIST);
--
--
--INSERT INTO ITEM (favorites_list_id, name, position)
--    SELECT 1, 'pizza', 1
--    WHERE NOT EXISTS (SELECT * FROM ITEM);
--
--INSERT INTO ITEM (favorites_list_id, name, position)
--    SELECT 1, 'corn', 2
--    WHERE NOT EXISTS (SELECT * FROM ITEM);
--
--INSERT INTO ITEM (favorites_list_id, name, position)
--    SELECT 1, 'tortillas', 3
--    WHERE NOT EXISTS (SELECT * FROM ITEM);


