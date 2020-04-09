## Updates to Project 1

### Section 5

Addition after page 10.

```
getTotalCopies(dId, month, year) --> int
getTotalPrice(dId, month, year) --> float
```

Returns the total number and price of each publication sold for a distributor for a given month.

```
getTotalRevenue() --> float
```

Returns the total revenue of the publishing house.

```
getTotalExpenses() --> float
```

Returns the total expenses of the publishing house.

```
getDistributorCount() --> int
```

Returns the number of distributors in the system.

```
getTotalRevenueByCity(city) --> float
getTotalRevenueByDistributor(dId) --> float
getTotalRevenueByLocation(addr) --> float
```

Returns the total revenue, since inception, by city, distributor, and location respectively.

```
getTotalPaymentsByTitle(title) --> float
getTotalPaymentsBetween(start, end) -->float
```

Returns the total payments by job title and between two dates.