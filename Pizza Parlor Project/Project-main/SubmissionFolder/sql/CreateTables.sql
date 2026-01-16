DROP SCHEMA IF EXISTS Pizza;
CREATE SCHEMA Pizza;
USE Pizza;

DROP TABLE IF EXISTS baseprice;
CREATE TABLE baseprice (
  baseprice_Size VARCHAR(30),
  baseprice_CrustType VARCHAR(30),
  baseprice_CustPrice DECIMAL(5,2) NOT NULL,
  baseprice_BusPrice DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (baseprice_Size, baseprice_CrustType)
);

DROP TABLE IF EXISTS topping;
CREATE TABLE topping (
	topping_TopID INT auto_increment,
    topping_TopName VARCHAR(30) NOT NULL,
    topping_SmallAMT DECIMAL(5,2) NOT NULL,
    topping_MedAMT DECIMAL (5,2) NOT NULL,
    topping_LgAMT DECIMAL (5,2) NOT NULL,
    topping_XLAMT DECIMAL (5,2) NOT NULL,
    topping_CustPrice DECIMAL (5,2) NOT NULL,
    topping_BusPrice DECIMAL (5,2) NOT NULL,
    topping_MinINVT INT NOT NULL,
    topping_CurINVT INT NOT NULL,
    PRIMARY KEY (topping_TopID)
);

DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
customer_CustID INT auto_increment,
customer_FNAME VARCHAR(30) NOT NULL,
customer_LName VARCHAR(30) NOT NULL,
customer_PhoneNum VARCHAR(30) NOT NULL,
PRIMARY KEY(customer_CustID)
);

DROP TABLE IF EXISTS ordertable;
CREATE TABLE ordertable (
	ordertable_OrderID int auto_increment,
    customer_CustID int,
    ordertable_OrderType VARCHAR(30) NOT NULL,
    ordertable_OrderDateTime DATETIME NOT NULL,
    ordertable_CustPrice DECIMAL(5,2) NOT NULL,
    ordertable_BusPrice DECIMAL(5,2) NOT NULL,
    ordertable_isComplete BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(ordertable_OrderID),
    FOREIGN KEY (customer_CustID) references customer(customer_CustID)
);

DROP TABLE IF EXISTS pizza;
CREATE TABLE pizza (
	pizza_PizzaID int auto_increment,
    pizza_Size VARCHAR(30) NOT NULL,
    pizza_CrustType VARCHAR(30) NOT NULL,
    pizza_PizzaState VARCHAR(30) NOT NULL,
    pizza_PizzaDate DATETIME NOT NULL,
    pizza_CustPrice DECIMAL(5,2) NOT NULL,
    pizza_BusPrice DECIMAL(5,2) NOT NULL,
    ordertable_ORDERID int NOT NULL,
	PRIMARY KEY(pizza_PizzaID),
    FOREIGN KEY(pizza_Size, pizza_CrustType) references baseprice(baseprice_Size, baseprice_CrustType),
    FOREIGN KEY(ordertable_ORDERID) references ordertable(ordertable_OrderID)
);

DROP TABLE IF EXISTS pizza_topping;
CREATE TABLE pizza_topping (
	pizza_PizzaID int NOT NULL,
    topping_TopID int NOT NULL,
    pizza_topping_IsDouble INT NOT NULL,
    PRIMARY KEY(pizza_PizzaID, topping_TopID),
    FOREIGN KEY(pizza_PizzaID) references pizza(pizza_PizzaID),
    FOREIGN KEY(topping_TopID) references topping(topping_TopID)
);

DROP TABLE IF EXISTS discount;
CREATE TABLE discount (
	discount_DiscountID int auto_increment,
    discount_DiscountName VARCHAR(30) NOT NULL,
    discount_Amount DECIMAL(5,2) NOT NULL,
    discount_IsPercent TINYINT NOT NULL,
    PRIMARY KEY(discount_DiscountID)
);

DROP TABLE IF EXISTS pizza_discount;
CREATE TABLE pizza_discount (
	pizza_PizzaID int NOT NULL,
    discount_DiscountID int NOT NULL,
    PRIMARY KEY(pizza_PizzaID, discount_DiscountID),
    FOREIGN KEY(pizza_PizzaID) references pizza(pizza_PizzaID),
    FOREIGN KEY(discount_DiscountID) references discount(discount_DiscountID)
);

DROP TABLE IF EXISTS order_discount;
CREATE TABLE order_discount (
	ordertable_OrderID int NOT NULL,
    discount_DiscountID int NOT NULL,
    PRIMARY KEY(ordertable_OrderID, discount_DiscountID),
    FOREIGN KEY(ordertable_OrderID) references ordertable(ordertable_OrderID),
    FOREIGN KEY(discount_DiscountID) references discount(discount_DiscountID)
);

DROP TABLE IF EXISTS pickup;
CREATE TABLE pickup (
	ordertable_OrderID int,
    pickup_IsPickedUp BOOLEAN not null DEFAULT FALSE,
    PRIMARY KEY(ordertable_OrderID),
    FOREIGN KEY(ordertable_OrderID) references ordertable(ordertable_OrderID)
);

DROP TABLE IF EXISTS delivery;
CREATE TABLE delivery (
	ordertable_OrderID int,
    delivery_HouseNum INT not null,
    delivery_Street VARCHAR(30) NOT NULL,
    delivery_City VARCHAR(30) NOT NULL,
    delivery_State VARCHAR(2) NOT NULL,
    delivery_Zip INT NOT NULL,
    delivery_IsDelivered BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY(ordertable_OrderID),
    FOREIGN KEY(ordertable_OrderID) references ordertable(ordertable_OrderID)
);

DROP TABLE IF EXISTS dinein;
CREATE TABLE dinein (
	ordertable_OrderID int,
    dinein_TableNum INT NOT NULL,
    PRIMARY KEY(ordertable_OrderID),
    FOREIGN KEY(ordertable_OrderID) references ordertable(ordertable_OrderID)
);






