# Test database container

For testing you can use docker-compose to setup a testing database.

## Starting the test dabatase

Just run:

```bash
  docker-compose up
```

The Database is listening on port: 3306

User is: root

Password: steve

Database is: sam

## Schema

Execute the following sql in the **sam** datbase

```mysql

DROP TABLE IF EXISTS logs;

CREATE TABLE logs (
    `id` int,
    `title` VARCHAR(255) NOT NULL,
    `application`VARCHAR(255) NOT NULL,
    `day` INT UNSIGNED NOT NULL,
    `month`INT UNSIGNED NOT NULL,  
	`year` INT UNSIGNED NOT NULL,
	`hour` INT UNSIGNED NOT NULL,
	`minute` INT UNSIGNED NOT NULL,
	`second` INT UNSIGNED NOT NULL,
	`Duration` INT UNSIGNED	
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

DROP TABLE IF EXISTS ActivityConstraints;

CREATE TABLE ActivityConstraints (
    `name` VARCHAR(255) NOT NULL,
    `keywords` VARCHAR(255),
    `application`VARCHAR(255) NOT NULL,
    `time_limit` INT UNSIGNED NOT NULL,
	`duration` INT UNSIGNED NOT NULL,
	`not_known` INT UNSIGNED NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;
```
