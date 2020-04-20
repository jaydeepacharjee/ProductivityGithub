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
