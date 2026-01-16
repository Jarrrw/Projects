CREATE VIEW ToppingPopularity AS
SELECT topping_TopName as "Topping", COALESCE(SUM(p.pizza_topping_isDouble), 0) as "ToppingCount"
FROM topping t left join pizza_topping p on t.topping_TopID = p.topping_TopID
GROUP BY topping_TopName
ORDER BY SUM(pizza_topping_isDouble) desc, topping_TopName asc;

CREATE VIEW ProfitByOrderType AS
SELECT 
    o.ordertable_OrderType AS CustomerType,
    DATE_FORMAT(o.ordertable_OrderDateTime, '%c/%Y') AS OrderMonth,
    ROUND(SUM(o.ordertable_CustPrice), 2) AS TotalOrderPrice,
    ROUND(SUM(o.ordertable_BusPrice), 2) AS TotalOrderCost,
    ROUND(SUM(o.ordertable_CustPrice) - SUM(o.ordertable_BusPrice), 2) AS Profit
FROM ordertable o
GROUP BY 
    o.ordertable_orderType,
    DATE_FORMAT(o.ordertable_orderDateTime, '%c/%Y')
UNION ALL
SELECT 
    '' AS CustomerType,
    'Grand Total' AS OrderMonth,
    ROUND(SUM(o.ordertable_CustPrice), 2),
    ROUND(SUM(o.ordertable_BusPrice), 2),
    ROUND(SUM(o.ordertable_CustPrice) - SUM(o.ordertable_BusPrice), 2)
FROM ordertable o
ORDER BY Profit asc;

CREATE VIEW ProfitByPizza AS
SELECT 
    p.pizza_Size AS Size,
    p.pizza_CrustType AS Crust,
    ROUND(SUM(p.pizza_CustPrice) - SUM(p.pizza_BusPrice), 2) AS Profit,
    DATE_FORMAT(p.pizza_PizzaDate, '%c/%Y') AS OrderMonth
FROM pizza p
GROUP BY 
	p.pizza_Size, 
    p.pizza_CrustType, 
    DATE_FORMAT(p.pizza_PizzaDate, '%c/%Y')
ORDER BY ROUND(SUM(p.pizza_CustPrice) - SUM(p.pizza_BusPrice), 2) ASC;

