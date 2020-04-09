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
INSERT INTO Staff(sname, age, gender, stype, phone, email, address) 
VALUES ('Chris', 32, "M", "staff author",  9095866774, "3004@gmail.com", "3311 CDE St, NC 27605");
INSERT INTO Staff(sname, age, gender, stype, phone, email, address) 
VALUES ('Ariel', 30, "F", "staff author",  9893869812, "3005@gmail.com", "125 Nory St, NC 27635");


CREATE TABLE Payments (
payid INT AUTO_INCREMENT PRIMARY KEY,
sid INT NOT NULL,
paycheck FLOAT NOT NULL,
paydate DATE NOT NULL,
FOREIGN KEY(sid) REFERENCES Staff(sid) ON UPDATE CASCADE
) AUTO_INCREMENT = 6001;

INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3001, 1000, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3003, 1000, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3003, 1200, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3004, 1100, "2020-04-01");
INSERT INTO Payments (sid, paycheck, paydate)
VALUES (3005, 1000, "2020-04-01");

-- price deleted from Publications
CREATE TABLE Publications (
pid INT AUTO_INCREMENT PRIMARY KEY,
ptype VARCHAR(30) NOT NULL,
title VARCHAR(250) NOT NULL,
editor VARCHAR(250),
topics VARCHAR(200),
dop DATE,
url VARCHAR(2048)
) AUTO_INCREMENT = 1001;

-- Publications --
INSERT INTO Publications (ptype, title, editor, topics, dop)
VALUES ('book', 'introduction to database', 'John', 'technology', '2018-10-10');
INSERT INTO Publications (ptype, title, editor, topics, dop)
VALUES ('magazine', 'Healthy Diet', 'Ethen', 'health', '2020-02-24');
INSERT INTO Publications (ptype, title, editor, topics, dop)
VALUES ('journal', 'Animal Science', '/', 'science', '2020-03-01');
INSERT INTO Publications (ptype, title, editor, topics, dop, url)
VALUES ('book', 'Food for Today', 'John', 'health', '2019-08-02', 'https://bit.ly/2xhTC1e');
INSERT INTO Publications(ptype, title, editor, topics, dop, url) 
VALUES ('magazine', 'Birds and Blooms', 'Ethen', 'nature', '2019-07-20', 'https://bit.ly/2vIN7o4');


CREATE TABLE Books(
pid INT NOT NULL,
ISBN VARCHAR(50) NOT NULL,
edition INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
ON DELETE CASCADE
);


INSERT INTO Books(pid, ISBN, edition) VALUES (1001, 12345, 2);
INSERT INTO Books(pid, ISBN, edition) VALUES (1004, 346752, 1);


CREATE TABLE Chapters(
pid INT NOT NULL,
chno INT NOT NULL,
chtitle VARCHAR(150) NOT NULL,
chtext LONGTEXT,
url VARCHAR(2048),
PRIMARY KEY(pid, chno),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

INSERT INTO Chapters(pid, chno, chtitle, chtext, url) 
VALUES (1001, 2, 'Query Reformulation', 'Semantic Query', 'https://bit.ly/tsrt112');
INSERT INTO Chapters(pid, chno, chtitle, chtext, url) 
VALUES (1001, 4, 'Query Execution', 'Query-execution process', 'https://bit.ly/3aa41uL');


CREATE TABLE PeriodicPublication (
pid INT NOT NULL,
periodicity VARCHAR(50) NOT NULL,
pptype VARCHAR(30) NOT NULL,
pptext LONGTEXT NOT NULL,
doi DATE,
FOREIGN KEY(pid) REFERENCES Publications(pid)
ON UPDATE CASCADE
);

INSERT INTO PeriodicPublication(pid, periodicity, pptype, pptext, doi) 
VALUES (1002, 'monthly', 'magazine', 'ABC', '2020-02-24');
INSERT INTO PeriodicPublication(pid, periodicity, pptype, pptext, doi) 
VALUES (1003, 'monthly', 'journal', 'AAA', '2020-03-01');
INSERT INTO PeriodicPublication(pid, periodicity, pptype, pptext, doi) 
VALUES (1005, 'weekly', 'magazine', 'Birds', '2020-01-07');

CREATE TABLE Issue(
pid INT NOT NULL,
ino INT NOT NULL,
PRIMARY KEY(pid, ino),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
ON DELETE CASCADE
);

INSERT INTO Issue (pid, ino)
VALUES (1002, 1);
INSERT INTO Issue (pid, ino)
VALUES (1003, 3);
INSERT INTO Issue (pid, ino)
VALUES (1005, 1);


-- atopics attribute was added
CREATE TABLE Articles(
aid INT AUTO_INCREMENT PRIMARY KEY,
atitle VARCHAR(350) NOT NULL,
atopics VARCHAR(100),
doc DATE,
atext MEDIUMTEXT NOT NULL,
url VARCHAR(2048)
) AUTO_INCREMENT = 5001;


INSERT INTO Articles(atitle, atopics, doc, atext, url) 
VALUES('Spatio-Temporal Database in Hospitals', 'database, health', '2018-09-02', 'Spatio-Temporal', 'https://bit.ly/3aRwIgq');
INSERT INTO Articles(atitle, atopics, doc, atext, url) 
VALUES('Miami Underwater', 'nature', '2019-12-14', 'Miami Beach', 'https://bit.ly/3dmeZiS');
INSERT INTO Articles(atitle, atopics, doc, atext, url) 
VALUES('Vanishing Act', 'science', '2018-12-27', 'Vanishing Act', 'https://bit.ly/3a8LLBH');


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
INSERT INTO Distributors (dname, dtype, address, city, phno, contact, tot_balance) 
VALUES ('Robinson', 'library', '1315 Oakwood Ave, NC', 'Raleigh', 9842314572, 'Andrew', 0);
INSERT INTO Distributors (dname, dtype, address, city, phno, contact, tot_balance) 
VALUES ('The Book Barn', 'bookstore', '410 Delaware St', 'Leavenworth', 9136826518, 'Charles', 0);


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
INSERT INTO Orders (copies, odate, deldate, price, shcost)
VALUES (40, '2020-02-20', '2020-03-10', 17, 20);


CREATE TABLE ConsistOf(
oid INT NOT NULL,
pid INT NOT NULL,
FOREIGN KEY(oid) REFERENCES Orders(oid) ON UPDATE CASCADE,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

INSERT INTO ConsistOf(oid, pid)
VALUES (4001, 1001);
INSERT INTO ConsistOf(oid, pid)
VALUES (4002, 1001);
INSERT INTO ConsistOf(oid, pid)
VALUES (4003, 1003);
INSERT INTO ConsistOf(oid, pid)
VALUES (4004, 1005);

CREATE TABLE MakeOrder(
did INT NOT NULL,
oid INT NOT NULL,
FOREIGN KEY(did) REFERENCES Distributors(did) ON UPDATE CASCADE,
FOREIGN KEY(oid) REFERENCES Orders(oid) ON UPDATE CASCADE
);

INSERT INTO MakeOrder(did, oid)
VALUES (2001, 4001);
INSERT INTO MakeOrder(did, oid)
VALUES (2001, 4002);
INSERT INTO MakeOrder(did, oid)
VALUES (2002, 4003);
INSERT INTO MakeOrder(did, oid)
VALUES (2003, 4004);


CREATE TABLE ContainArticle(
pid INT NOT NULL,
aid INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(aid) REFERENCES Articles(aid) ON UPDATE CASCADE
);

INSERT INTO ContainArticle(pid, aid)
VALUES (1002, 5001);
INSERT INTO ContainArticle(pid, aid)
VALUES (1003, 5003);
INSERT INTO ContainArticle(pid, aid)
VALUES (1005, 5002);


CREATE TABLE Edit(
pid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid)
ON UPDATE CASCADE
);

INSERT INTO Edit (pid, sid)
VALUES (1001, 3001);
INSERT INTO Edit (pid, sid)
VALUES (1002, 3002);
INSERT INTO Edit (pid, sid)
VALUES (1004, 3001);
INSERT INTO Edit (pid, sid)
VALUES (1005, 3002);

-- INSERT INTO Edit (pid, sid) VALUES (1002, 3001);


CREATE TABLE WriteArticle(
aid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(aid) REFERENCES Articles(aid) ON UPDATE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid)
ON UPDATE CASCADE
);

INSERT INTO WriteArticle (aid, sid)
VALUES (5001, 3004);
INSERT INTO WriteArticle (aid, sid)
VALUES (5002, 3003);
INSERT INTO WriteArticle (aid, sid)
VALUES (5003, 3004);

CREATE TABLE WriteBook(
pid INT NOT NULL,
sid INT NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(sid) REFERENCES Staff(sid)
ON UPDATE CASCADE
);

INSERT INTO WriteBook (pid, sid)
VALUES (1001, 3003);
INSERT INTO WriteBook (pid, sid)
VALUES (1004, 3005);


/**
CREATE TABLE Topics(
topic VARCHAR(50) PRIMARY KEY
);

INSERT INTO Topics (topic) VALUES ('technology');
INSERT INTO Topics (topic) VALUES ('health');
INSERT INTO Topics (topic) VALUES ('science');

CREATE TABLE HasTopic(
pid INT NOT NULL,
topic VARCHAR(50),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(topic) REFERENCES Topics(topic) ON UPDATE CASCADE
);

INSERT INTO HasTopic(pid, topic)
VALUES (1001, 'technology');
INSERT INTO HasTopic(pid, topic)
VALUES (1002, 'health');
INSERT INTO HasTopic(pid, topic)
VALUES (1003, 'science');
**/
