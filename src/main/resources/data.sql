START TRANSACTION;

INSERT IGNORE INTO users (id, username, password, email, role) VALUES (1, 'admin', '$2a$10$Sp1qWJ7WGTaNa0y6l2hVpOqSUSxAG/2owprdR34fD6YWZ/.CvqN9S', 'admin@admin.com', 'ROLE_ADMIN');
INSERT IGNORE INTO users (id, username, password, email, role) VALUES (2, 'user', '$2a$10$Sp1qWJ7WGTaNa0y6l2hVpOqSUSxAG/2owprdR34fD6YWZ/.CvqN9S', 'user@user.com', 'ROLE_USER');

INSERT IGNORE INTO regions (id, name) VALUES (1, 'London');
INSERT IGNORE INTO regions (id, name) VALUES (2, 'New York');
INSERT IGNORE INTO regions (id, name) VALUES (3, 'Berlin');

INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (1, 'London Ad 1', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (2, 'London Ad 2', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (3, 'London Ad 3', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (4, 'London Ad 4', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (5, 'London Ad 5', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (6, 'London Ad 6', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (7, 'London Ad 7', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='user'), (SELECT MAX(id) FROM regions WHERE name='London'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (8, 'New York Ad', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='admin'), (SELECT MAX(id) FROM regions WHERE name='New York'));
INSERT IGNORE INTO ads (id, title, description, created_at, updated_at, user_id, region_id) VALUES (9, 'Berlin Ad', "This is the Ad's description.", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT MAX(id) FROM users WHERE username='admin'), (SELECT MAX(id) FROM regions WHERE name='Berlin'));

COMMIT;
