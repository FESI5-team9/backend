-- data.sql

INSERT INTO USER_TABLE (email, password, name, company_name, image, created_at, updated_at, deleted_at)
VALUES
    ('user1@example.com', 'password1', 'test1', 'Company A', 'image1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('user2@example.com', 'password2', 'test2', 'Company B', 'image2.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('user3@example.com', 'password3', 'test3', 'Company C', 'image3.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);
