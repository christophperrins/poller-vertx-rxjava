create database if not exists pollervertx;
use pollervertx;
create table if not exists endpoints (id int primary key auto_increment, name varchar(50), endpoint text, frequency int, active boolean);
create table if not exists endpoint_history(id int primary key auto_increment, endpoint_id int, status varchar(3), response_time int, timedate datetime, foreign key(endpoint_id) references endpoints(id));
create table if not exists information(id int primary key auto_increment, endpoint_id int, owner varchar(40), street text, postcode varchar(10), city varchar(40), foreign key(endpoint_id) references endpoints(id));
