DROP DATABASE IF EXISTS setup;
CREATE DATABASE setup;
USE setup;


/**
DROP TABLE IF EXISTS Staff CASCADE;
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
sid INT NOT NULL AUTO_INCREMENT,
sname VARCHAR(50) NOT NULL,
stype VARCHAR(30) NOT NULL,
sdate DATE NOT NULL,
periodicity INT NOT NULL,
dob DATE NOT NULL,
gender VARCHAR(20) NOT NULL,
phone VARCHAR(20),
pay FLOAT NOT NULL,
PRIMARY KEY (sid, sdate)
);

-- Staff --
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Chris Harrington', 'author', '2014-05-15', 14, '1983-12-19', 'male', '909-586-6774', 250.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Pryanka Dittan', 'editor', '2014-05-15', 14, '1981-04-09', 'female', '919-436-1287', 280.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Rita Malavino', 'author', '2015-11-27', -1, '1990-10-21', 'female', '915-897-1653', 500.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Yun Tao', 'author', '2019-01-29', 28, '1991-02-20', 'male', '833-572-0648', 4930.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Robert Arwinski', 'editor', '2016-08-20', 14, '1968-07-17', 'male', '917-874-0271', 285.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Gandalf Gray', 'editor', '2014-05-15', -1, '1972-07-09', 'male', '905-963-0279', 654.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Bilbo Baggins', 'author', '2018-02-24', 28, '1981-11-21', 'male', '909-578-9264', 490.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Arwen Undomiel', 'author', '2016-09-01', -1, '1976-03-05', 'female', '895-565-8016', 500.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Samwise Gamgee', 'author', '2014-11-22', 28, '1984-08-01', 'male', '881-654-9072', 520.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Pippin Took', 'editor', '2015-08-12', 14, '1980-01-12', 'male', '792-548-0165', 280.0);
INSERT INTO Staff(sname, stype, sdate, periodicity, dob, gender, phone, pay) 
VALUES ('Ryan Kipling', 'author', '2014-06-10', 14, '1977-01-11', 'male', '342-549-1835', 265.0);



CREATE TABLE Publications (
pid INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(250) NOT NULL,
ptype VARCHAR(30) NOT NULL,
dop DATE NOT NULL,
url VARCHAR(2048) NOT NULL,
price FLOAT
);

CREATE TABLE Books(
pid INT PRIMARY KEY,
ISBN VARCHAR(50) NOT NULL,
edition INT,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

CREATE TABLE Chapters(
pid INT,
chno INT,
chtitle VARCHAR(150) NOT NULL,
url VARCHAR(2048) NOT NULL,
PRIMARY KEY(pid, chno),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

CREATE TABLE PeriodicPublication (
pid INT PRIMARY KEY,
periodicity INT NOT NULL,
pptype VARCHAR(30) NOT NULL,
FOREIGN KEY(pid) REFERENCES Publications(pid)
ON UPDATE CASCADE

);

CREATE TABLE Issue(
pid INT,
ino INT,
PRIMARY KEY(pid, ino),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

CREATE TABLE Articles(
aid INT AUTO_INCREMENT PRIMARY KEY,
atitle VARCHAR(350) NOT NULL,
doc DATE NOT NULL,
url VARCHAR(2048) NOT NULL
);

CREATE TABLE Topics(
topic VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Orders(
oid INT AUTO_INCREMENT PRIMARY KEY,
price FLOAT NOT NULL,
copies INT NOT NULL,
shcost FLOAT NOT NULL,
odate DATE NOT NULL
);

CREATE TABLE Distributors(
did INT AUTO_INCREMENT PRIMARY KEY,
dname VARCHAR(100) NOT NULL,
dtype VARCHAR(50) NOT NULL,
city VARCHAR(50) NOT NULL,
address VARCHAR(200) NOT NULL,
contact VARCHAR(50) NOT NULL,
phno VARCHAR(20) NOT NULL,
tot_balance FLOAT
);

CREATE TABLE ConsistOf(
oid INT,
pid INT,
FOREIGN KEY(oid) REFERENCES Orders(oid) ON UPDATE CASCADE,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE
);

CREATE TABLE MakeOrder(
did INT,
oid INT,
FOREIGN KEY(did) REFERENCES Distributors(did) ON UPDATE CASCADE,
FOREIGN KEY(oid) REFERENCES Orders(oid) ON UPDATE CASCADE
);

CREATE TABLE ContainArticle(
pid INT,
aid INT,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(aid) REFERENCES Articles(aid) ON UPDATE CASCADE
);

CREATE TABLE Edit(
pid INT,
sid INT,
sdate DATE,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(sid, sdate) REFERENCES Staff(sid, sdate)
ON UPDATE CASCADE
);

CREATE TABLE HasTopic(
pid INT,
topic VARCHAR(50),
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(topic) REFERENCES Topics(topic) ON UPDATE CASCADE
);

CREATE TABLE WriteArticle(
aid INT,
sid INT,
sdate DATE,
FOREIGN KEY(aid) REFERENCES Articles(aid) ON UPDATE CASCADE,
FOREIGN KEY(sid, sdate) REFERENCES Staff(sid, sdate)
ON UPDATE CASCADE
);

CREATE TABLE WriteBook(
pid INT,
sid INT,
sdate DATE,
FOREIGN KEY(pid) REFERENCES Publications(pid) ON UPDATE CASCADE,
FOREIGN KEY(sid, sdate) REFERENCES Staff(sid, sdate)
ON UPDATE CASCADE
);


-- Publications --
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Knowledge Engineering and Semantic Web', 'book', '2017-11-08','https://www.springer.com/gp/book/9783319695471', 37.12);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('SERVO', 'magazine', '2019-09-18','https://www.servomagazine.com/', 17.09);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Journal of Computer Science & Systems Biology', 'journal', '2016-10-01','https://www.omicsonline.org/computer-science-systems-biology.php', 27.99);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Insights in Biomedicine', 'journal', '2018-12-21','https://biomedicine.imedpub.com/', 46.69);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Database Design for Mere Mortals', 'book', '2014-09-05','https://www.goodreads.com/book/show/150062.Database_Design_for_Mere_Mortals', 18.78);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Nature Communications', 'journal', '2016-04-20','https://www.nature.com/ncomms/', 25.21);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Earth Interactions', 'journal', '2015-10-08','https://journals.ametsoc.org/toc/eint/19/12', 16.98);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Birds and Blooms', 'magazine', '2019-07-20','https://www.magazine.store/birds-and-blooms/', 26.19);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Make Your Own Neural Network ', 'book', '2016-03-31','https://www.barnesandnoble.com/w/make-your-own-neural-network-tariq-rashid/1123691651', 15.91);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Remote: Office Not Required', 'book', '2014-10-17','https://www.goodreads.com/book/show/17316682-remote', 18.99);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Bioinformatics and Functional Genomics', 'book', '2014-08-15', 'http://www.bioinfbook.org/php/?q=node/156', 35.09);
INSERT INTO Publications(title, ptype, dop, url, price) 
VALUES ('Food for Today', 'book', '2014-11-09', 'https://app.oncoursesystems.com/school/webpage/11515833/1490563', 30.08);

-- Books --
INSERT INTO Books(pid, ISBN, edition) VALUES (1, '89-15-28-237106-32', 2);
INSERT INTO Books(pid, ISBN, edition) VALUES (5, '08-25-287-94201-91', 1);
INSERT INTO Books(pid, ISBN, edition) VALUES (9, '92-14-123-89175-05', 3);
INSERT INTO Books(pid, ISBN, edition) VALUES (10, '978-3-319-69548-8', 1);
INSERT INTO Books(pid, ISBN, edition) VALUES (11, '23-92-597-21149-76', 3);
INSERT INTO Books(pid, ISBN, edition) VALUES (12, '18-48-2-86528-09', 2);

-- Periodic Publications --
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (2, 2, 'journal');
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (3, 1, 'magazine');
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (4, 2, 'journal');
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (6, 3, 'journal');
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (7, 6, 'journal');
INSERT INTO PeriodicPublication(pid, periodicity, pptype) VALUES (8, 3, 'magazine');

-- Issues --
INSERT INTO Issue (pid, ino) VALUES (2, 4);
INSERT INTO Issue (pid, ino) VALUES (3, 10);
INSERT INTO Issue (pid, ino) VALUES (4, 6);
INSERT INTO Issue (pid, ino) VALUES (6, 2);
INSERT INTO Issue (pid, ino) VALUES (7, 2);
INSERT INTO Issue (pid, ino) VALUES (8, 3);

-- Chapters --
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (1, 3, 'Diversified Semantic Query Reformulation', 'https://www.springer.com/gp/book/9783319695471');
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (5, 4, 'Query Execution', 'https://link.springer.com/chapter/10.1007/978-1-4302-4660-2_14');
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (9, 5, 'Pairwise Alignment', 'http://www.bioinfbook.org/php/?q=C3E3');
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (10, 2, 'Stop the commute', 'http://v.fastcdn.co/u/3a1b1cdf/28217077-0-Remote-Office-Not-Re.pdf');
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (11, 3, 'Reviewing Data Integrity', 'https://flylib.com/books/en/1.199.1.135/1/');
INSERT INTO Chapters(pid, chno, chtitle, url) 
VALUES (12, 6, 'Planning Daily Food Choices', 'https://www.mheducation.com/prek-12/product/food-today-student-edition-mcgraw-hill/9780026430487.html#tab-content-tableOfContents');

-- Articles --
INSERT INTO Articles(atitle, doc, url) 
VALUES ('The Second Term', '2014-06-05', 'https://www.newyorker.com/magazine/2012/06/18/the-second-term');
INSERT INTO Articles(atitle, doc, url) 
VALUES ('Come to Happyland', '2014-09-02', 'https://www.outsideonline.com/1910091/come-happyland?page=all');
INSERT INTO Articles(atitle, doc, url) 
VALUES('Miami Underwater', '2015-12-14', 'https://www.newyorker.com/magazine/2015/12/21/the-siege-of-miami');
INSERT INTO Articles(atitle, doc, url) 
VALUES('Philip Glass', '2016-10-20', 'https://www.interviewmagazine.com/music/philip-glass#_');
INSERT INTO Articles(atitle, doc, url) 
VALUES('Vanishing Act', '2014-12-27', 'http://www.slate.com/articles/arts/music_box/2009/12/vanishing_act.html');
INSERT INTO Articles(atitle, doc, url) 
VALUES('The Ghosts of the Glacier', '2018-10-09', 'https://www.gq.com/story/missing-parents-melting-glacier-swiss-alps');
INSERT INTO Articles(atitle, doc, url) 
VALUES('Jane Goodall Is Still Wild at Heart', '2019-08-08','https://www.nytimes.com/2015/03/15/magazine/jane-goodall-is-still-wild-at-heart.html');

-- Topics --
INSERT INTO Topics (topic) VALUES ('animals');
INSERT INTO Topics (topic) VALUES ('medicine');
INSERT INTO Topics (topic) VALUES ('sport');
INSERT INTO Topics (topic) VALUES ('music');
INSERT INTO Topics (topic) VALUES ('robotics');
INSERT INTO Topics (topic) VALUES ('computer science');
INSERT INTO Topics (topic) VALUES ('food');
INSERT INTO Topics (topic) VALUES ('discovery');
INSERT INTO Topics (topic) VALUES ('database');
INSERT INTO Topics (topic) VALUES ('psychology');
INSERT INTO Topics (topic) VALUES ('nature');


-- Orders --
INSERT INTO Orders (price, copies, shcost, odate) VALUES (18.98, 280, 15.51, '2019-08-01'); -- 7 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (19.09, 300, 15.51, '2019-12-03'); -- 2 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (17.91, 150, 10.29, '2018-09-01'); -- 9 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (28.19, 280, 23.51, '2019-08-01'); -- 8 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (39.12, 280, 15.51, '2018-03-10'); -- 1 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (32.08, 100, 16.78, '2018-09-10'); -- 12 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (29.99, 450, 12.11, '2017-10-05'); -- 3 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (28.19, 85, 24.59, '2020-01-15'); -- 8 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (39.12, 400, 10.29, '2018-01-12'); -- 1 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (17.91, 650, 15.51, '2017-11-29'); -- 9 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (48.69, 700, 29.99, '2019-06-30'); -- 4 pid
INSERT INTO Orders (price, copies, shcost, odate) VALUES (39.12, 550, 35.59, '2018-02-28'); -- 1 pid 
INSERT INTO Orders (price, copies, shcost, odate) VALUES (17.91, 550, 35.59, '2018-09-28'); -- 9 pid


-- Distributors --
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Prezell Robinson Library', 'library', 'Raleigh', '1315 Oakwood Ave', 'Andrew Hilmore', '984-231-4572', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Barnes & Noble', 'bookstore', 'Raleigh', '5959 Triangle Town Blvd Unit 2107', 'Nancy Mitchell', '919-792-2140', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Books By the Bay', 'bookstore', 'North Bend', '1875 Sherman Ave', 'Lily Moore', '541-756-1215', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Allen Public Library', 'library', 'Allen', '300 N Allen Dr', 'Christina Selano', '214-509-4900', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Anchor Distributors', 'wholesale', 'New Kensington', '1030 Hunt Valley Circle', 'Alicia Woods', '800-444-4484', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Gem Guides Books', 'wholesale', 'Upland', '1155 W. 9th Street', 'Elle Pierce', '626-855-1611', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Ingram Book', 'wholesale', 'La Vergne', 'One Ingram Blvd', 'Richard Dole', '615-793-5000', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('The Book Barn', 'bookstore', 'Leavenworth', '410 Delaware St', 'Charles Lewismare', '913-682-6518', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance)
VALUES ('Lawrence Public Library', 'library', 'New Lawrence', '707 Vermont St', 'Barbara Richardson', '785-843-3833', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('John C. Hodges Library', 'library', 'Knoxville', '1015 Volunteer Blvd', 'Alberta Boticelli', '865-974-4351', 0);
INSERT INTO Distributors (dname, dtype, city, address, contact, phno, tot_balance) 
VALUES ('Firestorm Books & Coffee', 'bookstore', 'Asheville', '610 Haywood Rd #B', 'Peter Barringtone', '828-255-8115', 0);

-- Edit --
INSERT INTO Edit (pid, sid, sdate) VALUES (2, 10, '2015-08-12');
INSERT INTO Edit (pid, sid, sdate) VALUES (1, 5, '2016-08-20');
INSERT INTO Edit (pid, sid, sdate) VALUES (4, 2, '2014-05-15');
INSERT INTO Edit (pid, sid, sdate) VALUES (11, 6, '2014-05-15');
INSERT INTO Edit (pid, sid, sdate) VALUES (6, 5, '2016-08-20');
INSERT INTO Edit (pid, sid, sdate) VALUES (3, 2, '2014-05-15');
INSERT INTO Edit (pid, sid, sdate) VALUES (5, 2, '2014-05-15');
INSERT INTO Edit (pid, sid, sdate) VALUES (7, 10, '2015-08-12');
INSERT INTO Edit (pid, sid, sdate) VALUES (8, 5, '2016-08-20');
INSERT INTO Edit (pid, sid, sdate) VALUES (9, 10, '2015-08-12');
INSERT INTO Edit (pid, sid, sdate) VALUES (10, 2, '2014-05-15');
INSERT INTO Edit (pid, sid, sdate) VALUES (12, 2, '2014-05-15');

-- WriteArticle --
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (1, 1, '2014-05-15');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (2, 11, '2014-06-10');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (3, 9, '2014-11-22');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (4, 3, '2015-11-27');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (5, 9, '2014-11-22');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (6, 7, '2018-02-24');
INSERT INTO WriteArticle (aid, sid, sdate) VALUES (7, 4, '2019-01-29');

-- WriteBook --
INSERT INTO WriteBook (pid, sid, sdate) VALUES (1, 8, '2016-09-01');
INSERT INTO WriteBook (pid, sid, sdate) VALUES (5, 1, '2014-05-15');
INSERT INTO WriteBook (pid, sid, sdate) VALUES (9, 9, '2014-11-22');
INSERT INTO WriteBook (pid, sid, sdate) VALUES (10, 11, '2014-06-10');
INSERT INTO WriteBook (pid, sid, sdate) VALUES (11, 1, '2014-05-15');
INSERT INTO WriteBook (pid, sid, sdate) VALUES (12, 11, '2014-06-10');


-- HasTopic --
INSERT INTO HasTopic (pid, topic) VALUES (1, 'computer science');
INSERT INTO HasTopic (pid, topic) VALUES (2, 'robotics');
INSERT INTO HasTopic (pid, topic) VALUES (3, 'computer science');
INSERT INTO HasTopic (pid, topic) VALUES (4, 'medicine');
INSERT INTO HasTopic (pid, topic) VALUES (5, 'database');
INSERT INTO HasTopic (pid, topic) VALUES (6, 'discovery');
INSERT INTO HasTopic (pid, topic) VALUES (7, 'nature');
INSERT INTO HasTopic (pid, topic) VALUES (8, 'nature');
INSERT INTO HasTopic (pid, topic) VALUES (9, 'computer science');
INSERT INTO HasTopic (pid, topic) VALUES (10, 'psychology');
INSERT INTO HasTopic (pid, topic) VALUES (11, 'medicine');
INSERT INTO HasTopic (pid, topic) VALUES (12, 'food');

-- MakeOrder --
INSERT INTO MakeOrder (did, oid) VALUES (1, 1);
INSERT INTO MakeOrder (did, oid) VALUES (5, 10);
INSERT INTO MakeOrder (did, oid) VALUES (3, 6);
INSERT INTO MakeOrder (did, oid) VALUES (8, 9);
INSERT INTO MakeOrder (did, oid) VALUES (7, 11);
INSERT INTO MakeOrder (did, oid) VALUES (10, 8);
INSERT INTO MakeOrder (did, oid) VALUES (4, 5);
INSERT INTO MakeOrder (did, oid) VALUES (1, 7);
INSERT INTO MakeOrder (did, oid) VALUES (2, 12);
INSERT INTO MakeOrder (did, oid) VALUES (3, 3);
INSERT INTO MakeOrder (did, oid) VALUES (4, 2);
INSERT INTO MakeOrder (did, oid) VALUES (6, 4);
INSERT INTO MakeOrder (did, oid) VALUES (3, 13);

-- ConsistOf --
INSERT INTO ConsistOf (oid, pid) VALUES (1, 7);
INSERT INTO ConsistOf (oid, pid) VALUES (10, 9);
INSERT INTO ConsistOf (oid, pid) VALUES (9, 1);
INSERT INTO ConsistOf (oid, pid) VALUES (11, 4);
INSERT INTO ConsistOf (oid, pid) VALUES (6, 12);
INSERT INTO ConsistOf (oid, pid) VALUES (8, 8);
INSERT INTO ConsistOf (oid, pid) VALUES (5, 1);
INSERT INTO ConsistOf (oid, pid) VALUES (7, 3);
INSERT INTO ConsistOf (oid, pid) VALUES (12, 1);
INSERT INTO ConsistOf (oid, pid) VALUES (3, 9);
INSERT INTO ConsistOf (oid, pid) VALUES (2, 2);
INSERT INTO ConsistOf (oid, pid) VALUES (4, 8);
INSERT INTO ConsistOf (oid, pid) VALUES (13, 9);


-- ContainArticle --
INSERT INTO ContainArticle (pid, aid) VALUES (2, 7);
INSERT INTO ContainArticle (pid, aid) VALUES (3, 3);
INSERT INTO ContainArticle (pid, aid) VALUES (4, 6);
INSERT INTO ContainArticle (pid, aid) VALUES (6, 2);
INSERT INTO ContainArticle (pid, aid) VALUES (7, 1);
INSERT INTO ContainArticle (pid, aid) VALUES (8, 4);
INSERT INTO ContainArticle (pid, aid) VALUES (7, 5);


