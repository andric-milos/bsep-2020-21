INSERT INTO `bsep`.`administrator` (`id`, `password`, `username`) VALUES ('1', 'admin', 'admin');

insert into `bsep`.`authority` (`name`) values ('ADMIN');
insert into `bsep`.`authority` (`name`) values ('USER');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `organization`, `password`, `user_role`, `city`, `country`)
values ('nikola@gmail.com', true, 'Nikola', 'Kekic', false, 'ftn',
'$2y$12$1xiQFNbAsdrLMrxEojq4QuGXbaP74Bam4lOBX7JDBQSfmrgNRMrOK', 'USER', 'Novi Sad', 'Serbia');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `organization`, `password`, `user_role`, `city`, `country`)
values ('admin@gmail.com', true, 'Admin', 'Admin', false, 'admin',
'$2y$12$h/A1KO4/EOdDd6I0G4tEE.KwEWlF5FIqMlGo/f108/qPJvTtW9QGW', 'ADMIN', 'Novi Sad', 'Serbia');

INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('1', '2');
INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('2', '1');
INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('2', '2');

INSERT INTO `bsep`.`issuer_and_subject_data`
(`id`, `alias`, `certificate_role`, `certificate_status`, `city_issuer`, `city_subject`,
`country_issuer`, `country_subject`, `email_issuer`, `email_subject`, `expiring_date`,
`first_name_issuer`, `first_name_subject`, `last_name_issuer`, `last_name_subject`,
`organization_issuer`, `organization_subject`, `parent_id`, `start_date`) VALUES
('1', '3757053589842', 'SELF_SIGNED', null, 'Novi Sad', 'Novi Sad',
'Serbia', 'Serbia', 'admin@gmail.com', 'admin@gmail.com', '2049-12-31 23:00:00.000000',
'Admin', 'Admin', 'Admin', 'Admin',
'admin', 'admin', '1', '1999-12-31 23:00:00.000000');

INSERT INTO `bsep`.`issuer_and_subject_data`
(`id`, `alias`, `certificate_role`, `certificate_status`, `city_issuer`, `city_subject`,
`country_issuer`, `country_subject`, `email_issuer`, `email_subject`, `expiring_date`,
`first_name_issuer`, `first_name_subject`, `last_name_issuer`, `last_name_subject`,
`organization_issuer`, `organization_subject`, `parent_id`, `start_date`) VALUES
('2', '1126310501329', 'INTERMEDIATE', null, 'Novi Sad', 'Novi Sad',
'Serbia', 'Serbia', 'admin@gmail.com', 'nikola@gmail.com', '2049-12-31 23:00:00.000000',
'Admin', 'Nikola', 'Admin', 'Kekic',
'admin', 'ftn', '1', '1999-12-31 23:00:00.000000');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `organization`, `password`, `user_role`, `city`, `country`)
values ('milos@gmail.com', true, 'Milos', 'Andric', false, 'ftn',
'$2y$12$1xiQFNbAsdrLMrxEojq4QuGXbaP74Bam4lOBX7JDBQSfmrgNRMrOK', 'USER', 'Novi Sad', 'Serbia');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `organization`, `password`, `user_role`, `city`, `country`)
values ('scholes@gmail.com', true, 'Paul', 'Scholes', false, 'manutd',
'$2y$12$1xiQFNbAsdrLMrxEojq4QuGXbaP74Bam4lOBX7JDBQSfmrgNRMrOK', 'USER', 'Manchester', 'England');

INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('3', '2');
INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('4', '2');

INSERT INTO `bsep`.`issuer_and_subject_data`
(`id`, `alias`, `certificate_role`, `certificate_status`, `city_issuer`, `city_subject`,
`country_issuer`, `country_subject`, `email_issuer`, `email_subject`, `expiring_date`,
`first_name_issuer`, `first_name_subject`, `last_name_issuer`, `last_name_subject`,
`organization_issuer`, `organization_subject`, `parent_id`, `start_date`) VALUES
('3', '1352002178193', 'END_ENTITY', null, 'Novi Sad', 'Novi Sad',
'Serbia', 'Serbia', 'milos@gmail.com', 'nikola@gmail.com', '2049-12-31 23:00:00.000000',
'Milos', 'Nikola', 'Andric', 'Kekic',
'ftn', 'ftn', '2', '1999-12-31 23:00:00.000000');

INSERT INTO `bsep`.`issuer_and_subject_data`
(`id`, `alias`, `certificate_role`, `certificate_status`, `city_issuer`, `city_subject`,
`country_issuer`, `country_subject`, `email_issuer`, `email_subject`, `expiring_date`,
`first_name_issuer`, `first_name_subject`, `last_name_issuer`, `last_name_subject`,
`organization_issuer`, `organization_subject`, `parent_id`, `start_date`) VALUES
('4', '977020317340', 'INTERMEDIATE', null, 'Novi Sad', 'Novi Sad',
'Serbia', 'Serbia', 'admin@gmail.com', 'milos@gmail.com', '2049-12-31 23:00:00.000000',
'Admin', 'Milos', 'Admin', 'Andric',
'admin', 'ftn', '1', '1999-12-31 23:00:00.000000');
