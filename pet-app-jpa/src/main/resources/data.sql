INSERT IGNORE INTO `roles` (`id`, `name`) VALUES
	(3, 'ROLE_ADMIN'),
	(2, 'ROLE_MENTOR'),
	(1, 'ROLE_USER');


INSERT IGNORE INTO `users` (`id`, `created_at`, `updated_at`, `address`, `alt_phone_no`, `date_of_join`, `email`, `name`, `password`, `phone_no`, `primary_skills`, `secondary_skills`, `soe`, `soe_ref`) VALUES
	(1, '2019-06-28 09:57:48', NULL, 'Aroha Technologies', NULL, NULL, 'admin@aroha.co.in', 'Admin', '$2a$10$7sFPuv1oAYlgVQSzzCdQwe5fo28SYUJZ7jsIdJJXtMfccghn7sknq', '9249876052', NULL, NULL, NULL, NULL);

INSERT IGNORE INTO `user_roles` (`user_id`, `role_id`) VALUES
	(1, 3);

INSERT IGNORE INTO `technology` (`tech_id`,`technology_name`) VALUES
        (1,'SQL'),
        (2,'C'),
        (3,'JAVA'),
        (4,'JAVASCRIPT'),
        (5,'PYTHON');

INSERT IGNORE INTO `price_model` (`price_id`, `course_category`, `course_description`, `course_image`, `course_name`, `course_price`, `course_rated_number_count`, `course_rating`) VALUES
	(1, 'Productivity', 'RDBMS Concepts + Oracle SQL + Access to test your capability in SQL', NULL, 'SQL Programming', 1000, '15', 4.5),
	(2, 'Productivity', 'RDBMS Concepts + Oracle SQL + Oracle PL/SQL + Access to test capability', NULL, 'Database Programming', 1500, '20', 4.5),
	(3, 'Productivity', 'SQL', NULL, 'SQL Programming', 300, '13', 4),
	(4, 'Productivity', 'Java', NULL, 'JAVA Programming', 300, '16', 4.5),
	(5, 'Productivity', 'C', NULL, 'C Programming', 300, '14', 4.5),
	(6, 'Productivity', 'Python', NULL, 'Python Programming', 300, '15', 4.5),
	(7, 'Productivity', 'JavaScript', NULL, 'JavaScript Programming', 300, '18', 4.5);