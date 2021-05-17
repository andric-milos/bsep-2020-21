INSERT INTO `bsep`.`administrator` (`id`, `password`, `username`) VALUES ('1', 'admin', 'admin');

INSERT INTO `bsep`.`issuer_and_subject_data`
(`first_name_issuer`, `last_name_issuer`,  `organization_issuer`, `country_issuer`,
 `city_issuer`, `email_issuer`, `type_of_entity`, `certificate_role`, `key_usage`,
 `extended_key_usage`, `certificate_status`, `parent_id`, `start_date`, `expiring_date`)
VALUES ( 'Marko', 'Markovic', 'FTN', 'RS', 'NS', 'marko@gmail.com',
'1', 'SELF_SIGNED',
null, null, 'VALID', 1, '2020-11-11 13:23:44', '2021-11-11 13:23:44' );

insert into `bsep`.`authority` (`name`) values ('ADMIN');
insert into `bsep`.`authority` (`name`) values ('USER');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `password`, `user_role`, `city`, `country`)
values ('nikola@gmail.com', true, 'Nikola', 'Kekic', false,
'$2y$12$1xiQFNbAsdrLMrxEojq4QuGXbaP74Bam4lOBX7JDBQSfmrgNRMrOK', 'USER', 'Novi Sad', 'Serbia');

insert into `bsep`.`users` (`email`, `enabled`, `first_name`, `last_name`, `locked`, `password`, `user_role`, `city`, `country`)
values ('admin@gmail.com', true, 'Admin', 'Admin', false,
'$2y$12$h/A1KO4/EOdDd6I0G4tEE.KwEWlF5FIqMlGo/f108/qPJvTtW9QGW', 'ADMIN', 'Novi Sad', 'Serbia');

INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('1', '2');
INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('2', '1');
INSERT INTO `bsep`.`user_authority` (`user_id`, `authority_id`) VALUES ('2', '2');
