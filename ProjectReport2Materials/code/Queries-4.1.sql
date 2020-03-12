/*

Staff (sid, name, pay, stype, phone, sdate, periodicity, dob, gender)
-- Authors (sid) --
-- Editors (sid) --
Topics (name)
Publications (pid, title, type, dop, url, price)
Books (pid, ISBN, edition)
Chapters (pid, chno, chtitle, url, +modified+)
Articles (aid, atitle, doc, url)
Periodic Publication (pid, periodicity, pptype, doi)
Issues (pid, ino)
Orders (oid, price, copies, shcost, odate)
Distributors (did, dname, dtype, tot_balance, phno, city, address, contact)
MakesOrder (did, oid)
ConsistOf (oid, pid, +type+)
ContainsArticles (pid, aid)
Edits (pid, sid)
WritesArticle (aid, sid)
WritesPublication (pid, sid)
HasTopic (pid, topic)
++ HasChapter(pid, cid)   // for chapters written but later removed

*/



-- NARRATIVE PART 1: EDITING AND PUBLISHING
-------------------------------------------
-- Enter basic information on a new publication
INSERT INTO Publications
VALUES (...);

INSERT INTO PeriodicPublications
VALUES (...);

INSERT INTO Books
VALUES (...);

INSERT INTO Chapters
VALUES (...);

INSERT INTO Articles
VALUES (...);

INSERT INTO HasTopic
VALUES (...);

INSERT INTO Topics
VALUES (...);

INSERT INTO Issues
VALUES (...);

-- Update information
UPDATE Publications
SET ...
WHERE ...;

UPDATE PeriodicPublications
SET ...
WHERE ...;

UPDATE Books
SET ...
WHERE ...;

DELETE FROM HasTopic
WHERE ...;

UPDATE Topics
SET ...
WHERE ...;

UPDATE Issues
SET ...
WHERE ...;

-- Assign editor(s) to publication
INSERT INTO Edits
VALUES (...);

-- Let each editor view the information on the publications he/she is responsible for
SELECT P.title, P.type, P.dop, P.url, P.price
FROM Publications P
NATURAL JOIN Edits E
WHERE E.sid = ...;

-- Edit table of contents of a publication, by adding/deleting articles (for periodic publications) or chapters/sections (for books). 
INSERT INTO ContainsArticles
VALUES (...);

DELETE FROM ContainsArticles
WHERE ...;

INSERT INTO HasChapter
VALUES (...);

DELETE FROM HasChapter
WHERE ...;

-- Production of a book edition or of an issue of a publication
---------------------------------------------------------------
-- Enter a new book edition or new issue of a publication
INSERT INTO Books
VALUES (...);   -- Assumption: new separate entry for new edition, i.e., all 3 fields make primary key

-- Update, delete a book edition or publication issue
UPDATE Books
SET ...
WHERE ...;

DELETE FROM Books
WHERE pid = ...
AND ISBN = ...
AND edition = ...;

/* Updating issue is done online, so no SQL query. */

DELETE FROM Issues
WHERE ...;

-- Enter/update an article or chapter: title, author's name, topic, and date
UPDATE Articles
SET title = ...
WHERE aid = ...;

UPDATE Articles
SET doc = NOW
WHERE aid = ...;

UPDATE WritesArticle
SET sid = (
    SELECT sid
    FROM Staff
    WHERE ...
)
WHERE aid = (
    SELECT aid
    FROM Articles
    WHERE ...
);

UPDATE Chapters
SET title = ...
WHERE pid = ...
AND chno = ...;

/* Can only change author and topic for books, not chapters. Add to assumptions. */

UPDATE Chapters
SET modified = NOW
WHERE pid = ...
AND chno = ...;

-- Enter/update text of an article
/* Assumption: online */

-- Find books and articles by topic, date, author's name
SELECT title, ISBN
FROM Books
NATURAL JOIN HasTopic
NATURAL JOIN Topics
NATURAL JOIN Publications
WHERE name = ...;

SELECT title, ISBN
FROM Publications
NATURAL JOIN Books
WHERE dop = ...;

SELECT title, ISBN
FROM Publications
NATURAL JOIN Books
NATURAL JOIN Staff S
NATURAL JOIN WritesPublication
WHERE S.name = ...;

SELECT atitle
FROM Articles
NATURAL JOIN ContainsArticles
NATURAL JOIN Topics
NATURAL JOIN PeriodicPublications
WHERE name = ...;

SELECT atitle
FROM Articles
WHERE doc = ...;

SELECT atitle
FROM Articles
NATURAL JOIN WritesArticle
NATURAL JOIN Staff
WHERE name = ...;

-- Enter payment for author or editor, and keep track of when each payment was claimed by its addressee. 
/* This isn't how our payment system works. We assume a direct deposit system where funds are automatically
disbursed according to a pay schedule. */

-- Distribution
-----------------------------------------------
-- Enter new distributor
INSERT INTO Distributors
VALUES (...)

-- Update distributor information
/* Assumption: cannot update distributor name */
UPDATE Distributors
SET dtype = ?????

UPDATE Distributors
SET tot_balance = tot_balance +/- ...
WHERE dname/did = ...;

UPDATE Distributors
SET phno = ...
WHERE did = ...;

UPDATE Distributors
SET city/address/contact = ...
WHERE dname/did = ...;

-- Delete a distributor
DELETE FROM Distributors
WHERE ...;

-- Input orders from distributors, for a book edition or an issue of a publication per distributor, for a certain date
INSERT INTO Orders
VALUES ...;

INSERT INTO MakesOrder
SELECT did, oid
FROM Distributors, Orders
WHERE (vals from above query)
AND did = (
    SELECT did
    FROM Distributors
    WHERE ...
);

INSERT INTO ConsistOf
SELECT oid, pid
FROM Orders, Publications
WHERE oid = ...
AND did = (
    SELECT did
    FROM Distributors
    WHERE ...
);

-- Bill distributor for an order
-- change outstanding balance of a distributor on receipt of a payment
/* Done above */

-- number and total price of copies of each publication bought per distributor per month;
SELECT did, MONTH(odate), dname, address, city, COUNT(*) AS Count
FROM Distributors
NATURAL JOIN MakesOrder
NATURAL JOIN Orders
GROUP BY did, MONTH(odate);

SELECT did, MONTH(odate), SUM(copies) * price + SUM(shcost) AS TotalPrice, dname, address, city
FROM Distributors
NATURAL JOIN MakesOrder
NATURAL JOIN Orders
GROUP BY did, MONTH(odate);

-- total revenue of the publishing house
WITH Profits AS (
    SELECT O.price - P.price AS Profit
    FROM Orders
    NATURAL JOIN ConsistOf
    NATURAL JOIN Publications
)
SELECT SUM(*) As TotalProfit
FROM Profits;

-- total expenses (i.e., shipping costs and salaries)
WITH ShippingCosts AS (
    SELECT shcost
    FROM Orders
),
ConstantSalaries AS (
    SELECT pay
    FROM Staff
    WHERE periodicity = -1
),
PeriodicSalaries AS (
    SELECT pay * ((NOW - sdate) / periodicity) AS TotalSalary
    FROM Staff
    WHERE periodicity <> -1
)
SELECT SUM(*) AS TotalCosts
FROM (
    (
        SELECT shcost AS cost
        FROM ShippingCosts
    ) UNION ALL (
        SELECT pay AS cost
        FROM ConstantSalaries
    ) UNION ALL (
        SELECT TotalSalary as cost
        FROM PeriodicSalaries
    )
);  

-- Calculate the total current number of distributors
SELECT COUNT(*)
FROM Distributors;

-- calculate total revenue (since inception) per city
WITH Profits AS (
    SELECT O.price - P.price AS Profit, city
    FROM Orders
    NATURAL JOIN ConsistOf
    NATURAL JOIN Publications
    NATURAL JOIN MakesOrder
    NATURAL JOIN Distributors
)
SELECT city, SUM(Profit) As Revenue
FROM Profits
GROUP BY city;

-- per distributor
WITH Profits AS (
    SELECT O.price - P.price AS Profit, did, dname
    FROM Orders
    NATURAL JOIN ConsistOf
    NATURAL JOIN Publications
    NATURAL JOIN MakesOrder
    NATURAL JOIN Distributors
)
SELECT did, SUM(*) As Revenue, dname
FROM Profits
GROUP BY did;

-- per location
WITH Profits AS (
    SELECT O.price - P.price AS Profit, address
    FROM Orders
    NATURAL JOIN ConsistOf
    NATURAL JOIN Publications
    NATURAL JOIN MakesOrder
    NATURAL JOIN Distributors
)
SELECT address, SUM(*) As Revenue, dname
FROM Profits
GROUP BY address;

-- Calculate total payments to the editors and authors, ???per time period??? and per work type
WITH ConstantSalaries AS (
    SELECT stype, pay
    FROM Staff
    WHERE periodicity = -1
),
PeriodicSalaries AS (
    SELECT stype, pay * ((NOW - sdate) / periodicity) AS pay
    FROM Staff
    WHERE periodicity <> -1
)
SELECT stype, SUM(pay)
FROM (
    SELECT *
    FROM ConstantSalaries
) UNION ALL (
    SELECT *
    FROM PeriodicSalaries
)
GROUP BY stype