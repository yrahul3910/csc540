DROP DATABASE IF EXISTS setup;
CREATE DATABASE setup;
USE setup;

/**
DROP TABLE IF EXISTS Staff CASCADE;
DROP TABLE IF EXISTS Payments CASCADE;
DROP TABLE IF EXISTS Publications CASCADE;
DROP TABLE IF EXISTS Books CASCADE;
DROP TABLE IF EXISTS Chapters CASCADE;
DROP TABLE IF EXISTS PeriodicPublication CASCADE;
DROP TABLE IF EXISTS Issue CASCADE;
DROP TABLE IF EXISTS Articles CASCADE;
DROP TABLE IF EXISTS Topics CASCADE;
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS Distributors CASCADE;

DROP TABLE IF EXISTS Edit CASCADE;
DROP TABLE IF EXISTS WriteArticle CASCADE;
DROP TABLE IF EXISTS WriteBook CASCADE;
DROP TABLE IF EXISTS ContainArticle CASCADE;
DROP TABLE IF EXISTS ConsistOf CASCADE;
DROP TABLE IF EXISTS HasTopic CASCADE;

**/

-- Create tables --

CREATE TABLE Staff (
sid INT AUTO_INCREMENT NOT NULL,
sname VARCHAR(50) NOT NULL,
age INT NOT NULL,
gender VARCHAR(1) NOT NULL,
stype VARCHAR(50) NOT NULL,
phone BIGINT,
email VARCHAR(100) NOT NULL,
address VARCHAR(100) NOT NULL,
PRIMARY KEY (sid)
) AUTO_INCREMENT = 3001;

-- Staff --
INSERT INTO Staff(sname, age, gender, stype, phone, email, address )
VALUES ("John", 36, "M", "staff editor", 9391234567, "3001@gmail.com", "21 ABC St, NC 27");
INSERT INTO Staff(sname, age, gender, stype, phone, email, address)
VALUES ("Ethen", 30, "M", "staff editor", 9491234567, "3002@gmail.com", "21 ABC St, NC 27606");
INSERT INTO Staff(sname, age, gender, stype, phone, email, address)
VALUES ("Cathy", 28, "F", "invited author", 9591234567, "3003@gmail.com", "3300 AAA St, NC 27606");


CREATE TABLE Payments (
payid INT AUTO_INCREMENT PRIMARY KEY,
sid INT NOT NULL,
paycheck FLOAT NOT NULL,
paydate DATE NOT NULL,
FOREIGN KEY(sid) REFERENCES Staff(sid) 
ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 6001;

INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3001, 1000, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3002, 1000, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3003, 1200, "2020-04-01");


CREATE TABLE Publications (
pid INT AUTO_INCREMENT PRIMARY KEY,
ptype VARCHAR(30) NOT NULL,
title VARCHAR(250) NOT NULL,
editor VARCHAR(250),
url VARCHAR(2048)
) AUTO_INCREMENT = 1001;

-- Publications --
INSERT INTO Publications (ptype, title, editor)
VALUES ('book', 'introduction to database', 'John');
INSERT INTO Publications (ptype, title, editor)
VALUES ('magazine', 'Healthy Diet', 'Ethen');
INSERT INTO Publications (ptype, title, editor)
VALUES ('journal', 'Animal Science', '/');


CREATE TABLE Books(
pid INT NOT NULL,
ISBN VARCHAR(50) NOT NULL,
edition INT NOT NULL,
dop DATE,
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(pid)
);


INSERT INTO Books(pid, ISBN, edition, dop) VALUES (1001, 12345, 2, '2018-10-10');


CREATE TABLE Chapters(
pid INT NOT NULL,
chno INT NOT NULL,
chtitle VARCHAR(150) NOT NULL,
chtext LONGTEXT,
url VARCHAR(2048),
PRIMARY KEY(pid, chno),
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO Chapters(pid, chno, chtitle, chtext, url) 
VALUES (1001, 2, 'Query Reformulation', 'Semantic Query', 'https://bit.ly/tsrt112');
INSERT INTO Chapters(pid, chno, chtitle, chtext, url) 
VALUES (1001, 4, 'Query Execution', 'Query-execution process', 'https://bit.ly/3aa41uL');


CREATE TABLE PeriodicPublication (
pid INT NOT NULL,
periodicity VARCHAR(50) NOT NULL,
pptext LONGTEXT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid)
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(pid)
);

INSERT INTO PeriodicPublication(pid, periodicity, pptext) 
VALUES (1002, 'monthly', 'ABC');
INSERT INTO PeriodicPublication(pid, periodicity, pptext) 
VALUES (1003, 'monthly', 'AAA');


CREATE TABLE Issue(
pid INT NOT NULL,
doi DATE NOT NULL,
INDEX (doi),
PRIMARY KEY(pid, doi),
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO Issue (pid, doi)
VALUES (1002, '2020-02-24');
INSERT INTO Issue (pid, doi)
VALUES (1003, '2020-03-01');


CREATE TABLE Articles(
aid INT AUTO_INCREMENT PRIMARY KEY,
atitle VARCHAR(350) NOT NULL,
doc DATE,
atext MEDIUMTEXT NOT NULL,
url VARCHAR(2048)
) AUTO_INCREMENT = 5001;


INSERT INTO Articles(atitle, doc, atext, url) 
VALUES('Spatio-Temporal Database in Hospitals', '2018-09-02', 'Spatio-Temporal', 'https://bit.ly/3aRwIgq');
INSERT INTO Articles(atitle, doc, atext, url) 
VALUES('Miami Underwater', '2019-12-14', 'Miami Beach', 'https://bit.ly/3dmeZiS');
INSERT INTO Articles(atitle, doc, atext, url) 
VALUES('Vanishing Act', '2018-12-27', 'Vanishing Act', 'https://bit.ly/3a8LLBH');


CREATE TABLE Distributors(
did INT AUTO_INCREMENT PRIMARY KEY,
dname VARCHAR(100) NOT NULL,
dtype VARCHAR(50) NOT NULL,
address VARCHAR(200) NOT NULL,
city VARCHAR(50) NOT NULL,
phno BIGINT NOT NULL,
contact VARCHAR(50) NOT NULL,
tot_balance FLOAT
) AUTO_INCREMENT = 2001;

INSERT INTO Distributors (dname, dtype, address, city, phno, contact, tot_balance) 
VALUES ('BookSell', 'bookstore', '2200, A Street, NC', 'Charlotte', 9191234567, 'Jason', 215);
INSERT INTO Distributors (dname, dtype, address, city, phno, contact, tot_balance) 
VALUES ('BookDist', 'wholesaler', '2200, B Street, NC', 'Raleigh', 9291234567, 'Alex', 0);


CREATE TABLE Orders(
oid INT AUTO_INCREMENT PRIMARY KEY,
copies INT NOT NULL,
odate DATE NOT NULL,
deldate DATE NOT NULL,
price FLOAT NOT NULL,
shcost FLOAT NOT NULL
) AUTO_INCREMENT = 4001;

INSERT INTO Orders (copies, odate, deldate, price, shcost)
VALUES (30, '2020-01-02', '2020-01-15', 20, 30);
INSERT INTO Orders (copies, odate, deldate, price, shcost)
VALUES (10, '2020-02-05', '2020-02-15', 20, 15);
INSERT INTO Orders (copies, odate, deldate, price, shcost)
VALUES (10, '2020-02-10', '2020-02-25', 10, 15);


CREATE TABLE ConsistOf(
oid INT NOT NULL,
pid INT NOT NULL,
FOREIGN KEY(oid) REFERENCES Orders(oid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(oid, pid)
);

INSERT INTO ConsistOf(oid, pid)
VALUES (4001, 1001);
INSERT INTO ConsistOf(oid, pid)
VALUES (4002, 1001);
INSERT INTO ConsistOf(oid, pid)
VALUES (4003, 1003);


CREATE TABLE MakeOrder(
did INT NOT NULL,
oid INT NOT NULL,
FOREIGN KEY(did) REFERENCES Distributors(did) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(oid) REFERENCES Orders(oid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(did, oid)
);

INSERT INTO MakeOrder(did, oid)
VALUES (2001, 4001);
INSERT INTO MakeOrder(did, oid)
VALUES (2001, 4002);
INSERT INTO MakeOrder(did, oid)
VALUES (2002, 4003);


CREATE TABLE ContainArticle(
pid INT NOT NULL,
aid INT NOT NULL,
doi DATE NOT NULL,
PRIMARY KEY(pid, aid, doi),
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(aid) REFERENCES Articles(aid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(doi) REFERENCES Issue(doi) 
ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO ContainArticle(pid, aid, doi)
VALUES (1002, 5001, '2020-02-24');
INSERT INTO ContainArticle(pid, aid, doi)
VALUES (1003, 5003, '2020-03-01');


CREATE TABLE Edit(
pid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(pid, sid)
);

INSERT INTO Edit (pid, sid)
VALUES (1001, 3001);
INSERT INTO Edit (pid, sid)
VALUES (1002, 3002);


CREATE TABLE WriteArticle(
aid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(aid) REFERENCES Articles(aid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(aid, sid)
);


CREATE TABLE WriteBook(
pid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(pid, sid)
);


CREATE TABLE Topics(
topic VARCHAR(50) PRIMARY KEY
);

INSERT INTO Topics (topic) VALUES ('technology');
INSERT INTO Topics (topic) VALUES ('health');
INSERT INTO Topics (topic) VALUES ('science');

CREATE TABLE HasTopic(
pid INT NOT NULL,
topic VARCHAR(50),
FOREIGN KEY(pid) REFERENCES Publications(pid) 
ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(topic) REFERENCES Topics(topic) 
ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY(pid, topic)
);

INSERT INTO HasTopic(pid, topic)
VALUES (1001, 'technology');
INSERT INTO HasTopic(pid, topic)
VALUES (1002, 'health');
INSERT INTO HasTopic(pid, topic)
VALUES (1003, 'science');
