CREATE TABLE person_type(
	person_type_id smallserial PRIMARY KEY,
	person_type_name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE person(
	person_id serial PRIMARY KEY,
	person_type_id smallint REFERENCES person_type (person_type_id), 
	person_username varchar(50) NOT NULL UNIQUE,
	person_password varchar(50) NOT NULL,
	person_name varchar(50) NOT NULL
);

CREATE TABLE ownership_status(
	ownership_status_id smallserial PRIMARY KEY,
	ownership_status_name varchar(30) NOT NULL UNIQUE
);

CREATE TABLE account(
	account_id serial PRIMARY KEY, 
	account_balance numeric(30,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE ownership_link(
	ownership_link_id serial PRIMARY KEY,
	person_id int REFERENCES person (person_id), 
	account_id int REFERENCES account (account_id), 
	ownership_status_id smallint REFERENCES ownership_status (ownership_status_id)
);

CREATE TABLE transaction_type(
	transaction_type_id smallserial PRIMARY KEY,
	transaction_type_name varchar(30) NOT NULL UNIQUE
);

CREATE TABLE balance_log(
	balance_log_id bigserial PRIMARY KEY,
	account_id int REFERENCES account (account_id),
	old_account_balance numeric(30,2) NOT NULL,
	new_account_balance numeric(30,2) NOT NULL,
	balance_log_time timestamp with time zone default clock_timestamp()
);

CREATE TABLE transaction_log(
	transaction_log_id bigserial PRIMARY KEY,
	triggered_by_person_id int REFERENCES person (person_id),
	affected_account_id int REFERENCES account (account_id),
	transaction_type_id int REFERENCES transaction_type (transaction_type_id),
	transaction_amount numeric (30,2) NOT NULL,
	transaction_log_time timestamp with time zone default clock_timestamp()
);

CREATE TABLE status_log(
	status_log_id serial PRIMARY KEY,
	modified_by_person_id int REFERENCES person (person_id),
	ownership_link_id smallint REFERENCES ownership_link (ownership_link_id),
	old_ownership_status_id smallint REFERENCES ownership_status (ownership_status_id),
	new_ownership_status_id smallint REFERENCES ownership_status (ownership_status_id),
	status_log_time timestamp with time zone default clock_timestamp()
);

INSERT INTO person_type(person_type_name)
Values ('Customer'),
('Employee'),
('Admin');

commit;

INSERT INTO person(person_type_id, person_username, person_password, person_name)
Values (1, 'Froddo', 'Baggins', 'Froddo Baggins'),
(1, 'Sam', 'Wise', 'Sam Wise'),
(1, 'Mary', 'Took', 'Mary Took'),
(1, 'Pippin', 'Took', 'Pippin Took'),
(2, 'Strider', 'Gondor', 'Aragorn'),
(3, 'The Grey', 'Balrog', 'Gandalf');

commit;

INSERT INTO ownership_status(ownership_status_name)
Values ('Approved'),
('Denied'),
('Pending Approval'),
('Pending Join'),
('Canceled');

commit;

INSERT INTO account(account_balance)
Values (200000), --Froddo
(100000), --Sam
(5), --Mary
(2000), --Pippin
(500), --Tooks joint
(500000000), --Aragorn
(8000000); --Gandalf

commit;

INSERT INTO ownership_link(person_id, account_id, ownership_status_id)
Values (1, 1, 1),
(2, 2, 1),
(3, 3, 2),
(3, 5, 3),
(4, 4, 1),
(4, 5, 2),
(5, 6, 1),
(6, 7, 1);

INSERT INTO transaction_type(transaction_type_name)
Values ('withdraw'),
('deposit'),
('transfer source'),
('transfer destination'),
('admin withdraw'),
('admin deposit'),
('admin transfer source'),
('admin transfer destination');
 
commit;

INSERT INTO balance_log(account_id, old_account_balance, new_account_balance)
Values (3, 20, 5);

INSERT INTO transaction_log(triggered_by_person_id, affected_account_id, transaction_type_id, transaction_amount)
Values (3, 3, 1, 15);

INSERT INTO status_log(modified_by_person_id, ownership_link_id, old_ownership_status_id, new_ownership_status_id)
Values (6, 2, 2, 1 );
commit;

/*
Drop table status_log;
Drop table transaction_log;
Drop table balance_log;
Drop table transaction_type;
Drop table ownership_link;
Drop table account;
Drop table ownership_status;
Drop table person;
Drop table person_type;
DROP VIEW account_info;
DROP FUNCTION get_account_info_by_id;
--*/



CREATE OR REPLACE VIEW account_info as
Select
per.person_id,
acc.account_id,
per.person_username as username,
per.person_name as full_name,
pt.person_type_name as person_type,
acc.account_balance as balance,
os.ownership_status_name as account_status
FROM ownership_link as ol
JOIN person as per on per.person_id = ol.person_id
JOIN person_type as pt on pt.person_type_id = per.person_type_id
JOIN account as acc on acc.account_id = ol.account_id
JOIN ownership_status as os on os.ownership_status_id = ol.ownership_status_id
ORDER BY per.person_id,
acc.account_id;
SELECT * FROM ownership_link WHERE ownership_status_id = 4 AND person_id = 23
commit;

CREATE OR REPLACE FUNCTION get_account_info_by_id(_id TEXT)
RETURNS TABLE(
	person_id int,
	account_id int,
	username TEXT,
	full_name TEXT,
	person_type TEXT,
	balance Numeric,
	account_status TEXT
) as
$$
SELECT * From account_info
WHERE person_id = _id::int;
$$ LANGUAGE SQL;
commit;

CREATE FUNCTION create_account(_person_id int, _status_id int)
RETURNS TABLE(
	account_id int
) as
$$
INSERT INTO account(account_balance)
Values (0)
RETURNING account_id as new_id;
INSERT INTO ownership_link(person_id, account_id, ownership_status_id)
Values(_person_id, 
	   (SELECT 
	   MAX(account_id) 
	   FROM account), _status_id);
SELECT MAX(account_id) FROM account;
$$ LANGUAGE SQL;

commit;


/*

SELECT * FROM account_info

SELECT count(*) FROM account

DELETE FROM ownership_link WHERE account_id = 1 AND person_id = 1;

SELECT (create_account(1, 3));

SELECT * from get_account_info_by_id('3');

UPDATE ownership_link set ownership_status_id = 3 
WHERE ownership_status_id is null

SELECT * FROM ownership_link WHERE ownership_status_id = 3;

SELECT count(*) FROM account
JOIN ownership_link on account.account_id = ownership_link.account_id
WHERE ownership_link.person_id = 4
AND ownership_link.ownership_status_id =1;

UPDATE account *
Set account_balance = 10 
SELECT * FROM account
WHERE account_id = 3
SELECT * FROM ownership_link WHERE  person_id = 15
Select account_balance FROM account WHERE account_id = 11
JOIN ownership_link on account.account_id = ownership_link.account_idWHERE ownership_link.person_id = 2;
*/
/*
Select * from person_type

Select * from person

Select * from ownership_status

Select * from account
count(*) from account

Select * from ownership_link

Select * from transaction_type

Select * from balance_log

Select * from transaction_log

Select * from status_log
--*/