INSERT INTO "member" (member_id, member_name, `password`, last_logged_in_at, is_deleted, description, created_at,
                      modified_at, modified_by)
VALUES (1, 'admin', '62ccffd497a0657aed27250f175bccd4bf03d40a8e4f1404b62ea6b0858217d5', '2020-01-01 00:00:00', 0,
        'Administrator', '2020-01-01 00:00:00', '2020-01-01 00:00:00', 'admin');
INSERT INTO "member" (member_id, member_name, `password`, last_logged_in_at, is_deleted, description, created_at,
                      modified_at, modified_by)
VALUES (2, 'admin2', '62ccffd497a0657aed27250f175bccd4bf03d40a8e4f1404b62ea6b0858217d5', '2020-01-01 00:00:00', 0,
        'Administrator', '2020-01-01 00:00:00', '2020-01-01 00:00:00', 'admin');

INSERT INTO "product" (product_id, member_id, name, image_url, description, is_deleted, created_at, modified_at)
VALUES (1, 1, 'Product 1', 'https://via.placeholder.com/150', 'Product 1 Description', 0, '2020-01-01 00:00:00',
        '2020-01-01 00:00:00');
INSERT INTO "product" (product_id, member_id, name, image_url, description, is_deleted, created_at, modified_at)
VALUES (2, 1, 'Product 2', 'https://via.placeholder.com/150', 'Product 2 Description', 0, '2021-01-01 00:00:00',
        '2021-01-01 00:00:00');
INSERT INTO "product" (product_id, member_id, name, image_url, description, is_deleted, created_at, modified_at)
VALUES (3, 2, 'Product 3', 'https://via.placeholder.com/150', 'Product 2 Description', 0, '2021-01-01 00:00:00',
        '2021-01-01 00:00:00');
