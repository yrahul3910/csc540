lines 259 - 263 

SELECT did, MONTH(odate), dname, address, city, COUNT(*) AS Count
FROM Distributors
NATURAL JOIN MakesOrder
NATURAL JOIN Orders
GROUP BY did, MONTH(odate);

-- MONTH doesn't work because years can be different -- 

-- number and total price of copies of each publication bought per distributor per month;
Select did, oid, pid, dname, COUNT(*) as number_orders, SUM(copies) as number_copies, Round(SUM(copies)*price) as total_price, odate
From
(Select did, oid, pid, dname, copies, price, DATE_FORMAT(odate, '%m-%Y') AS odate
From Distributors Natural join Makeorder natural join orders natural join consistof) t
Group by did, pid, t.odate;
-- I think we don't need adress, city here--

--total revenue--
select Round(SUM(copies)*t.price) as Revenue
from publications p, (select * from orders natural join consistof) t
where p.pid = t.pid;


4.2 

-- show all employees whose salary is less than 300$ --

EXPLAIN SELECT sid, sname
    -> FROM staff
    -> WHERE pay < 300;
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | staff | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   11 |    33.33 | Using where |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)

ALTER TABLE staff ADD INDEX(PAY)

EXPLAIN SELECT sid, sname
    -> FROM staff
    -> WHERE pay < 300;
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+-----------------------+
| id | select_type | table | partitions | type  | possible_keys | key  | key_len | ref  | rows | filtered | Extra                 |
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+-----------------------+
|  1 | SIMPLE      | staff | NULL       | range | pay           | pay  | 4       | NULL |    5 |   100.00 | Using index condition |
+----+-------------+-------+------------+-------+---------------+------+---------+------+------+----------+-----------------------+
1 row in set, 1 warning (0.00 sec)

