USE PIZZA;
INSERT INTO baseprice values
("Small", "Thin", 3, 0.5),
("Small", "Original", 3, 0.75),
("Small", "Pan", 3.5, 1),
("Small", "Gluten-Free", 4, 2),
("Medium", "Thin", 5, 1),
("Medium", "Original", 5, 1.5),
("Medium", "Pan", 6, 2.25),
("Medium", "Gluten-Free", 6.25, 3),
("Large", "Thin", 8, 1.25),
("Large", "Original", 8, 2),
("Large", "Pan", 9, 3),
("Large", "Gluten-Free", 9.5, 4),
("XLarge", "Thin", 10, 2),
("XLarge", "Original", 10, 3),
("XLarge", "Pan", 11.5, 4.5),
("XLarge", "Gluten-Free", 12.5, 6);

INSERT INTO topping (
    topping_TopName, topping_SmallAMT, topping_MedAMT,
    topping_LgAMT, topping_XLAMT, topping_CustPrice,
    topping_BusPrice, topping_MinINVT, topping_CurINVT
)
Values
("Pepperoni", 2, 2.75, 3.5, 4.5, 1.25, 0.2, 50, 100),
("Sausage", 2.5, 3, 3.5, 4.25, 1.25, 0.15, 50, 100),
("Ham", 2, 2.5, 3.25, 4, 1.5, 0.15, 25, 78),
("Chicken", 1.5, 2, 2.25, 3, 1.75, .25, 25, 56),
("Green Pepper", 1, 1.5, 2, 2.5, .5, .02, 25, 79),
("Onion", 1, 1.5, 2, 2.75, .5, .02, 25, 85),
("Roma Tomato", 2, 3, 3.5, 4.5, .75, .03, 10, 86),
("Mushrooms", 1.5, 2, 2.5, 3, .75, .1, 50, 52),
("Black Olives", .75, 1, 1.5, 2, .6, .1, 25, 39),
("Pineapple", 1, 1.25, 1.75, 2, 1, .25, 0, 15),
("Jalapenos", .5, .75, 1.25, 1.75, .5, .05, 0, 64),
("Banana Peppers", .6, 1, 1.3, 1.75, .5, .05, 0, 36),
("Regular Cheese", 2, 3.5, 5, 7, .5, .12, 50, 250),
("Four Cheese Blend", 2, 3.5, 5, 7, 1, .15, 25, 150),
("Feta Cheese", 1.75, 3, 4, 5.5, 1.5, .18, 0, 75),
("Goat Cheese", 1.6, 2.75, 4, 5.5, 1.5, .2, 0, 54),
("Bacon", 1, 1.5, 2, 3, 1.5, .25, 0, 89); 

INSERT INTO discount (
	discount_DiscountName,
    discount_Amount,
    discount_IsPercent
) values
("Employee", 15, 1),
("Lunch Special Medium", 1, 0),
("Lunch Special Large", 2, 0),
("Specialty Pizza", 1.5, 0),
("Happy Hour", 10, 1),
("Gameday Special", 20, 1);


INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (null, "dinein", "2025-01-05 12:03:00", 19.75, 3.68, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO dinein (ordertable_OrderID, dinein_TableNum)
VALUES (@new_person_id, 21);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Thin", "completed", "2025-01-05 12:03:00", 19.75, 3.68, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID)
VALUES (@new_pizza_id, 4);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 2, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 2);


INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (null, "dinein", "2025-02-03 12:05:00", 19.78, 4.63, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO dinein (ordertable_OrderID, dinein_TableNum)
VALUES (@new_person_id, 4);
INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID)
VALUES (@new_person_id, 5);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Medium", "Pan", "completed", "2025-02-03 12:05:00", 13.85, 3.23, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID)
VALUES (@new_pizza_id, 6);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 15, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 9, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 7, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 12, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 8, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Small", "Original", "completed", "2025-02-03 12:05:00", 6.93, 1.40, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 4, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 12, 1);

INSERT INTO customer (customer_FNAME, customer_LNAME, customer_PhoneNum)
Values ("Andrew", "Wilkes-Krier", "8642545861");
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (@new_person_id, "pickup", "2025-01-03 21:30:00", 89.28, 19.8, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO pickup (ordertable_OrderID, pickup_IsPickedUp)
VALUES (@new_person_id, true);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Original", "completed", "2025-01-03 21:30:00", 14.88, 3.30, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);

INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (1, "delivery", "2025-02-20 19:11:00", 68.95, 17.39, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO delivery (ordertable_OrderID, delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered)
VALUES (@new_person_id, 115, "Party Blvd", "Anderson", "SC", 29621, true);
INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID)
VALUES (@new_person_id, 2);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("XLarge", "Original", "completed", "2025-02-20 19:11:00", 27.94, 5.59, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 2, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 14, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("XLarge", "Original", "completed", "2025-02-20 19:11:00", 31.50, 6.25, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 14, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 3, 2);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 10, 2);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("XLarge", "Original", "completed", "2025-02-20 19:11:00", 26.75, 5.55, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID)
VALUES (@new_pizza_id, 6);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 14, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 4, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 17, 1);

INSERT INTO customer (customer_FNAME, customer_LNAME, customer_PhoneNum)
Values ("Matt", "Engers", "8644749953");
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (@new_person_id, "pickup", "2025-01-02 17:30:00", 28.70, 7.84, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO pickup (ordertable_OrderID, pickup_IsPickedUp)
VALUES (@new_person_id, true);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("XLarge", "Gluten-Free", "completed", "2025-01-02 17:30:00", 28.70, 7.84, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID)
VALUES (@new_pizza_id, 6);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 5, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 6, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 7, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 8, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 9, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 16, 1);

INSERT INTO customer (customer_FNAME, customer_LNAME, customer_PhoneNum)
Values ("Frank", "Turner", "8642328944");
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (@new_person_id, "delivery", "2025-01-02 18:17:00", 25.81, 3.64, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO delivery (ordertable_OrderID, delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered)
VALUES (@new_person_id, 6745, "Wessex St", "Anderson", "SC", 29621, true);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Thin", "completed", "2025-01-02 18:17:00", 25.81, 3.64, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 4, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 5, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 6, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 8, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 14, 2);

INSERT INTO customer (customer_FNAME, customer_LNAME, customer_PhoneNum)
Values ("Milo", "Auckerman", "8648785679");
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)
VALUES (@new_person_id, "delivery", "2025-02-13 20:32:00", 31.66, 6, true);
SET @new_person_id = LAST_INSERT_ID();
INSERT INTO delivery (ordertable_OrderID, delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered)
VALUES (@new_person_id, 8879, "Suburban Lane", "Anderson", "SC", 29621, true);
INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID)
VALUES (@new_person_id, 1);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Thin", "completed", "2025-02-13 20:32:00", 18.00, 2.75, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 14, 2);
INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID)
VALUES ("Large", "Thin", "completed", "2025-02-13 20:32:00", 19.25, 3.25, @new_person_id);
SET @new_pizza_id = LAST_INSERT_ID();
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 13, 1);
INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)
VALUES (@new_pizza_id, 1, 2);