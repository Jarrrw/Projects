
package cpsc4620;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * This file is where you will implement the methods needed to support this application.
 * You will write the code to retrieve and save information to the database and use that
 * information to build the various objects required by the applicaiton.
 *
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 *
 * This class also has static string variables for pickup, delivery and dine-in.
 * DO NOT change these constant values.
 *
 * You can add any helper methods you need, but you must implement all the methods
 * in this class and use them to complete the project.  The autograder will rely on
 * these methods being implemented, so do not delete them or alter their method
 * signatures.
 *
 * Make sure you properly open and close your DB connections in any method that
 * requires access to the DB.
 * Use the connect_to_db below to open your connection in DBConnector.
 * What is opened must be closed!
 */

/*
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
    private static Connection conn;

    // DO NOT change these variables!
    public final static String pickup = "pickup";
    public final static String delivery = "delivery";
    public final static String dine_in = "dinein";

    public final static String size_s = "Small";
    public final static String size_m = "Medium";
    public final static String size_l = "Large";
    public final static String size_xl = "XLarge";

    public final static String crust_thin = "Thin";
    public final static String crust_orig = "Original";
    public final static String crust_pan = "Pan";
    public final static String crust_gf = "Gluten-Free";

    public enum order_state {
        PREPARED,
        DELIVERED,
        PICKEDUP
    }


    private static boolean connect_to_db() throws SQLException, IOException
    {

        try {
            conn = DBConnector.make_connection();
            return true;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

    }

    public static void addOrder(Order o) throws SQLException, IOException
    {
        /*
         * add code to add the order to the DB. Remember that we're not just
         * adding the order to the order DB table, but we're also recording
         * the necessary data for the delivery, dinein, pickup, pizzas, toppings
         * on pizzas, order discounts and pizza discounts.
         *
         * This is a KEY method as it must store all the data in the Order object
         * in the database and make sure all the tables are correctly linked.
         *
         * Remember, if the order is for Dine In, there is no customer...
         * so the cusomter id coming from the Order object will be -1.
         *
         */
        connect_to_db();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            int customerID = o.getCustID();
            query = "INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            if (o.getCustID() == -1) {
                os.setNull(1, java.sql.Types.INTEGER);
            }
            else {
                os.setInt(1, o.getCustID());
            }
            os.setString(2, o.getOrderType());
            os.setTimestamp(3, Timestamp.valueOf(o.getDate()));
            os.setDouble(4, o.getCustPrice());
            os.setDouble(5, o.getBusPrice());
            os.setBoolean(6, o.getIsComplete());
            int rows = os.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = os.getGeneratedKeys()) {
                    if (rs.next()) {
                        o.setOrderID(rs.getInt(1));
                    }
                }
            }

            if (o instanceof DineinOrder) {
                DineinOrder order = (DineinOrder) o;
                query = "INSERT INTO dinein (ordertable_OrderID, dinein_TableNum)\n" +
                        "VALUES (?, ?)";
                os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                os.setInt(1, o.getOrderID());
                os.setInt(2, order.getTableNum());
                rows = os.executeUpdate();
            }
            else if (o instanceof  DeliveryOrder) {
                DeliveryOrder order = (DeliveryOrder) o;
                String[] address = order.getAddress().split("\t");
                query = "INSERT INTO delivery (ordertable_OrderID, delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                os.setInt(1, o.getOrderID());
                if (address.length == 5) {
                    os.setString(2, address[0]);
                    os.setString(3, address[1]);
                    os.setString(4, address[2]);
                    os.setString(5, address[3]);
                    os.setString(6, address[4]);
                } else {
                    throw new SQLException("Invalid address format");
                }
                os.setBoolean(7, false);
                rows = os.executeUpdate();
            }
            else if (o instanceof PickupOrder) {
                PickupOrder order = (PickupOrder) o;
                query = "INSERT INTO pickup (ordertable_OrderID, pickup_IsPickedUp)\n" +
                        "VALUES (?, ?)";
                os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                os.setInt(1, o.getOrderID());
                os.setBoolean(2, order.getIsPickedUp());
                rows = os.executeUpdate();
            }

            ArrayList<Pizza> pizzas = o.getPizzaList();
            Timestamp timestamp = Timestamp.valueOf(o.getDate());
            Date date = new Date(timestamp.getTime());
            for (Pizza p : pizzas) {
                addPizza(date, o.getOrderID(), p);
            }

            ArrayList<Discount> discounts = o.getDiscountList();
            for (Discount d: discounts) {
                query = "INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID)\n" +
                        "VALUES (?, ?)";
                os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                os.setInt(1, o.getOrderID());
                os.setInt(2, d.getDiscountID());
                rows = os.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return;
    }

    public static int addPizza(java.util.Date d, int orderID, Pizza p) throws SQLException, IOException
    {
        /*
         * Add the code needed to insert the pizza into into the database.
         * Keep in mind you must also add the pizza discounts and toppings
         * associated with the pizza.
         *
         * NOTE: there is a Date object passed into this method so that the Order
         * and ALL its Pizzas can be assigned the same DTS.
         *
         * This method returns the id of the pizza just added.
         *
         */
        connect_to_db();
        int pizzaID = -1;
        int row = 0;
        try {
            PreparedStatement os;
            ResultSet rset;
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());
            String query;
            query = "Insert into pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_ORDERID) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            os.setString(1, p.getSize());
            os.setString(2, p.getCrustType());
            os.setString(3,p.getPizzaState());
            os.setDate(4, sqlDate);
            os.setDouble(5, p.getCustPrice());
            os.setDouble(6, p.getBusPrice());
            os.setInt(7, orderID);
            row = os.executeUpdate();

            if (row > 0) {
                try (ResultSet rs = os.getGeneratedKeys()) {
                    if (rs.next()) {
                        pizzaID = rs.getInt(1);
                    }
                }
            }
            for (Discount dis : p.getDiscounts()) {
                query = "Insert into pizza_discount values (?,?)";
                os = conn.prepareStatement(query);
                os.setInt(1, pizzaID);
                os.setInt(2, dis.getDiscountID());
                os.executeUpdate();
            }

            for (Topping top : p.getToppings()) {
                query = "Insert into pizza_topping values (?,?,?)";
                os = conn.prepareStatement(query);
                os.setInt(1, pizzaID);
                os.setInt(2, top.getTopID());
                if (top.getDoubled()) {
                    os.setInt(3, 2);
                }
                else {
                    os.setInt(3, 1);
                }
                os.executeUpdate();
                double amount = 0;
                if (Objects.equals(p.getSize(), size_s)) {
                    if (top.getDoubled()) {
                        amount = 2 * top.getSmallAMT();
                    } else {
                        amount = top.getSmallAMT();
                    }
                }
                else if (Objects.equals(p.getSize(), size_m)){
                    if (top.getDoubled()) {
                        amount = (2 * top.getMedAMT());
                    } else {
                        amount = top.getMedAMT();
                    }
                }
                else if (Objects.equals(p.getSize(), size_l)) {
                    if (top.getDoubled()) {
                        amount = (2 * top.getLgAMT());
                    } else {
                        amount = top.getLgAMT();
                    }
                }
                else if (Objects.equals(p.getSize(), size_xl)) {
                    if (top.getDoubled()) {
                        amount = (2 * top.getXLAMT());
                    } else {
                        amount = top.getXLAMT();
                    }
                }
                addToInventory(top.getTopID(), -1 * amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return pizzaID;
    }

    public static int addCustomer(Customer c) throws SQLException, IOException
    {
        /*
         * This method adds a new customer to the database.
         *
         */
        connect_to_db();
        int ID = 0;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "INSERT INTO customer (customer_FName, customer_LName, customer_PhoneNum)\n" +
                    "VALUES (?, ?, ?)";
            os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            os.setString(1, c.getFName());
            os.setString(2, c.getLName());
            os.setString(3, c.getPhone());
            int rows = os.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = os.getGeneratedKeys()) {
                    if (rs.next()) {
                        ID = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return ID;
    }

    public static void completeOrder(int OrderID, order_state newState ) throws SQLException, IOException
    {
        /*
         * Mark that order as complete in the database.
         * Note: if an order is complete, this means all the pizzas are complete as well.
         * However, it does not mean that the order has been delivered or picked up!
         *
         * For newState = PREPARED: mark the order and all associated pizza's as completed
         * For newState = DELIVERED: mark the delivery status
         * FOR newState = PICKEDUP: mark the pickup status
         *
         */
        connect_to_db();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            if (newState == order_state.PREPARED) {
                query = "UPDATE ordertable\n" +
                        "SET ordertable_isComplete = 1\n" +
                        "WHERE ordertable_OrderID = ?";
                os = conn.prepareStatement(query);
                os.setInt(1, OrderID);
                os.executeUpdate();
                String query2;
                query2 = "UPDATE pizza\n" +
                        "SET pizza_PizzaState = 'completed'\n" +
                        "WHERE ordertable_OrderID = ?";
                os = conn.prepareStatement(query2);
                os.setInt(1, OrderID);
                os.executeUpdate();
                String query3 = "UPDATE delivery\n" +
                        "SET delivery_IsDelivered = '0'\n" +
                        "WHERE ordertable_OrderID = ?";
                os = conn.prepareStatement(query3);
                os.setInt(1, OrderID);
                os.executeUpdate();

            } else if (newState == order_state.DELIVERED) {
                query = "UPDATE delivery\n" +
                        "SET delivery_IsDelivered = 1\n" +
                        "WHERE ordertable_OrderID = ?";
                os = conn.prepareStatement(query);
                os.setInt(1, OrderID);
                os.executeUpdate();
            } else {
                query = "UPDATE pickup\n" +
                        "SET pickup_IsPickedUp = 1\n" +
                        "WHERE ordertable_OrderID = ?";
                os = conn.prepareStatement(query);
                os.setInt(1, OrderID);
                os.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
    }


    public static ArrayList<Order> getOrders(int status) throws SQLException, IOException
    {
        /*
         * Return an ArrayList of orders.
         * 	status   == 1 => return a list of open (ie oder is not completed)
         *           == 2 => return a list of completed orders (ie order is complete)
         *           == 3 => return a list of all the orders
         * Remember that in Java, we account for supertypes and subtypes
         * which means that when we create an arrayList of orders, that really
         * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
         *
         * You must fully populate the Order object, this includes order discounts,
         * and pizzas along with the toppings and discounts associated with them.
         *
         * Don't forget to order the data according to their order sequence, ie, order 1, order 2, etc.
         *
         */
        connect_to_db();
        ArrayList<Order> o = new ArrayList<Order>();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query2;
            if (status == 1) {
                query2 = "Select * from ordertable where ordertable_isComplete=false;";
            }
            else if (status == 2) {
                query2 = "Select * from ordertable where ordertable_isComplete=true;";
            }
            else {
                query2 = "Select * from ordertable;";
            }
            os = conn.prepareStatement(query2);;
            rset = os.executeQuery();
            while(rset.next()) {
                PreparedStatement os1;
                ResultSet rset1;
                Order order = null;
                if (Objects.equals(rset.getString("ordertable_OrderType"), dine_in)) {
                    String query = "Select * from dinein where ordertable_OrderID=?";
                    os1 = conn.prepareStatement("SELECT * FROM dinein WHERE ordertable_OrderID=?");
                    os1.setInt(1, rset.getInt("ordertable_OrderID"));

                    rset1 = os1.executeQuery();
                    if (!rset1.next()) continue;  // or throw error

                    order = new DineinOrder(
                            rset.getInt("ordertable_OrderID"),
                            rset.getInt("customer_CustID"),
                            rset.getString("ordertable_OrderDateTime"),
                            rset.getDouble("ordertable_CustPrice"),
                            rset.getDouble("ordertable_BusPrice"),
                            rset.getBoolean("ordertable_isComplete"),
                            rset1.getInt("dinein_TableNum")
                    );
                } else if (Objects.equals(rset.getString("ordertable_OrderType"), pickup)) {
                    os1 = conn.prepareStatement("SELECT * FROM pickup WHERE ordertable_OrderID=?");
                    os1.setInt(1, rset.getInt("ordertable_OrderID"));

                    rset1 = os1.executeQuery();
                    if (!rset1.next()) continue;

                    order = new PickupOrder(
                            rset.getInt("ordertable_OrderID"),
                            rset.getInt("customer_CustID"),
                            rset.getString("ordertable_OrderDateTime"),
                            rset.getDouble("ordertable_CustPrice"),
                            rset.getDouble("ordertable_BusPrice"),
                            rset1.getBoolean("pickup_IsPickedUp"),
                            rset.getBoolean("ordertable_isComplete")
                    );
                } else if (Objects.equals(rset.getString("ordertable_OrderType"), delivery)) {
                    os1 = conn.prepareStatement("SELECT * FROM delivery WHERE ordertable_OrderID=?");
                    os1.setInt(1, rset.getInt("ordertable_OrderID"));

                    rset1 = os1.executeQuery();
                    if (!rset1.next()) continue;

                    String address =
                            rset1.getInt("delivery_HouseNum") + "\t" +
                                    rset1.getString("delivery_Street") + "\t" +
                                    rset1.getString("delivery_City") + "\t" +
                                    rset1.getString("delivery_State") + "\t" +
                                    rset1.getString("delivery_Zip");

                    order = new DeliveryOrder(
                            rset.getInt("ordertable_OrderID"),
                            rset.getInt("customer_CustID"),
                            rset.getString("ordertable_OrderDateTime"),
                            rset.getDouble("ordertable_CustPrice"),
                            rset.getDouble("ordertable_BusPrice"),
                            rset.getBoolean("ordertable_isComplete"),
                            rset1.getBoolean("delivery_IsDelivered"),
                            address
                    );

                }
                order.setPizzaList(DBNinja.getPizzas(order));
                order.setDiscountList(DBNinja.getDiscounts(order));
                o.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return o;
    }

    public static Order getLastOrder() throws SQLException, IOException
    {
        /*
         * Query the database for the LAST order added
         * then return an Order object for that order.
         * NOTE...there will ALWAYS be a "last order"!
         */
        connect_to_db();
        Order o = null;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "SELECT *\n" +
                    "FROM ordertable\n" +
                    "ORDER BY ordertable_OrderID DESC\n" +
                    "LIMIT 1";
            os = conn.prepareStatement(query);;
            rset = os.executeQuery();
            if (rset.next()) {  // <-- MUST call next()!
                o = getOrderWithType(rset);
                o.setDiscountList(DBNinja.getDiscounts(o));
                o.setPizzaList(DBNinja.getPizzas(o));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return o;
    }

    public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException
    {
        /*
         * Query the database for ALL the orders placed on a specific date
         * and return a list of those orders.
         *
         */
        connect_to_db();
        ArrayList<Order> o = new ArrayList<Order>();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "SELECT *\n" +
                    "FROM ordertable\n" +
                    "WHERE DATE(ordertable_OrderDateTime) = ?";
            os = conn.prepareStatement(query);
            os.setString(1, date);
            rset = os.executeQuery();
            while(rset.next()) {
                Order order = getOrderWithType(rset);
                order.setDiscountList(DBNinja.getDiscounts(order));
                order.setPizzaList(DBNinja.getPizzas(order));
                if (order instanceof DineinOrder) {
                    order = (DineinOrder) order;
                } else if (order instanceof PickupOrder) {
                    order = (PickupOrder) order;
                } else if (order instanceof DeliveryOrder) {
                    order = (DeliveryOrder) order;
                }
                o.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return o;
    }

    private static Order getOrderWithType(ResultSet rset) throws SQLException, IOException {
        connect_to_db();
        PreparedStatement os1;
        ResultSet rset1;
        Order order = null;
        if (Objects.equals(rset.getString("ordertable_OrderType"), dine_in)) {
            os1 = conn.prepareStatement("SELECT * FROM dinein WHERE ordertable_OrderID=?");
            os1.setInt(1, rset.getInt("ordertable_OrderID"));

            rset1 = os1.executeQuery();
            if (!rset1.next()) return null;  // or throw error

            order = new DineinOrder(
                    rset.getInt("ordertable_OrderID"),
                    rset.getInt("customer_CustID"),
                    rset.getString("ordertable_OrderDateTime"),
                    rset.getDouble("ordertable_CustPrice"),
                    rset.getDouble("ordertable_BusPrice"),
                    rset.getBoolean("ordertable_isComplete"),
                    rset1.getInt("dinein_TableNum")
            );
        } else if (Objects.equals(rset.getString("ordertable_OrderType"), pickup)) {
            os1 = conn.prepareStatement("SELECT * FROM pickup WHERE ordertable_OrderID=?");
            os1.setInt(1, rset.getInt("ordertable_OrderID"));

            rset1 = os1.executeQuery();
            if (!rset1.next()) return null;

            order = new PickupOrder(
                    rset.getInt("ordertable_OrderID"),
                    rset.getInt("customer_CustID"),
                    rset.getString("ordertable_OrderDateTime"),
                    rset.getDouble("ordertable_CustPrice"),
                    rset.getDouble("ordertable_BusPrice"),
                    rset1.getBoolean("pickup_IsPickedUp"),
                    rset.getBoolean("ordertable_isComplete")
            );
        } else if (Objects.equals(rset.getString("ordertable_OrderType"), delivery)) {
            os1 = conn.prepareStatement("SELECT * FROM delivery WHERE ordertable_OrderID=?");
            os1.setInt(1, rset.getInt("ordertable_OrderID"));

            rset1 = os1.executeQuery();
            if (!rset1.next()) return null;

            String address =
                    rset1.getInt("delivery_HouseNum") + "\t" +
                            rset1.getString("delivery_Street") + "\t" +
                            rset1.getString("delivery_City") + "\t" +
                            rset1.getString("delivery_State") + "\t" +
                            rset1.getString("delivery_Zip");

            order = new DeliveryOrder(
                    rset.getInt("ordertable_OrderID"),
                    rset.getInt("customer_CustID"),
                    rset.getString("ordertable_OrderDateTime"),
                    rset.getDouble("ordertable_CustPrice"),
                    rset.getDouble("ordertable_BusPrice"),
                    rset1.getBoolean("delivery_IsDelivered"),
                    rset.getBoolean("ordertable_isComplete"),
                    address
            );

        }
        return order;
    }

    public static ArrayList<Discount> getDiscountList() throws SQLException, IOException
    {
        /*
         * Query the database for all the available discounts and
         * return them in an arrayList of discounts ordered by discount name.
         *
         */
        connect_to_db();
        ArrayList<Discount> d = new ArrayList<Discount>();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "SELECT *\n" +
                    "FROM discount\n" +
                    "ORDER BY discount_DiscountName";
            os = conn.prepareStatement(query);
            rset = os.executeQuery();
            while(rset.next())
            {
                d.add(new Discount(rset.getInt("discount_DiscountID"),
                        rset.getString("discount_DiscountName"),
                        rset.getDouble("discount_Amount"),
                        rset.getBoolean("discount_IsPercent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return d;
    }

    public static Discount findDiscountByName(String name) throws SQLException, IOException
    {
        /*
         * Query the database for a discount using it's name.
         * If found, then return an OrderDiscount object for the discount.
         * If it's not found....then return null
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();

        Discount d = null;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from discount where discount_DiscountName=?;";
            os = conn.prepareStatement(query1);
            os.setString(1, name);
            rset = os.executeQuery();
            while (rset.next()) {
                d = new Discount(rset.getInt("discount_DiscountID"),
                        rset.getString("discount_DiscountName"),
                        rset.getDouble("discount_Amount"),
                        rset.getBoolean("discount_IsPercent")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return d;
    }


    public static ArrayList<Customer> getCustomerList() throws SQLException, IOException
    {
        /*
         * Query the data for all the customers and return an arrayList of all the customers.
         * Don't forget to order the data coming from the database appropriately.
         *
         */
        connect_to_db();
        ArrayList<Customer> c = new ArrayList<Customer>();
        Statement stmt = conn.createStatement();

        try {
            PreparedStatement os;
            ResultSet rset;
            String query2;
            query2 = "Select * from customer order by customer_LName asc, customer_FName asc, customer_PhoneNum asc;";
            os = conn.prepareStatement(query2);;
            rset = os.executeQuery();
            while(rset.next())
            {
                c.add(new Customer(
                        rset.getInt("customer_CustID"),
                        rset.getString("customer_FName"),
                        rset.getString("customer_LName"),
                        rset.getString("customer_PhoneNum")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return c;
    }

    public static Customer findCustomerByPhone(String phoneNumber)  throws SQLException, IOException {
        /*
         * Query the database for a customer using a phone number.
         * If found, then return a Customer object for the customer.
         * If it's not found....then return null
         *
         */


        connect_to_db();
        Statement stmt = conn.createStatement();

        Customer c = null;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from customer where customer_PhoneNum=?;";
            os = conn.prepareStatement(query1);
            os.setString(1, phoneNumber);
            rset = os.executeQuery();
            while (rset.next()) {
                c = new Customer(rset.getInt("customer_CustID"), rset.getString("customer_FName"), rset.getString("customer_LName"), rset.getString("customer_PhoneNum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return c;
    }

    public static String getCustomerName(int CustID) throws SQLException, IOException
    {
        /*
         * COMPLETED...WORKING Example!
         *
         * This is a helper method to fetch and format the name of a customer
         * based on a customer ID. This is an example of how to interact with
         * your database from Java.
         *
         * Notice how the connection to the DB made at the start of the
         *
         */

        connect_to_db();

        /*
         * an example query using a constructed string...
         * remember, this style of query construction could be subject to sql injection attacks!
         *
         */
        String cname1 = "";
        String cname2 = "";
        String query = "Select customer_FName, customer_LName From customer WHERE customer_CustID=" + CustID + ";";
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        while(rset.next())
        {
            cname1 = rset.getString(1) + " " + rset.getString(2);
        }

        /*
         * an BETTER example of the same query using a prepared statement...
         * with exception handling
         *
         */
        try {
            PreparedStatement os;
            ResultSet rset2;
            String query2;
            query2 = "Select customer_FName, customer_LName From customer WHERE customer_CustID=?;";
            os = conn.prepareStatement(query2);
            os.setInt(1, CustID);
            rset2 = os.executeQuery();
            while(rset2.next())
            {
                cname2 = rset2.getString("customer_FName") + " " + rset2.getString("customer_LName"); // note the use of field names in the getSting methods
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }


        return cname1;
        // OR
        // return cname2;

    }


    public static ArrayList<Topping> getToppingList() throws SQLException, IOException
    {
        /*
         * Query the database for the aviable toppings and
         * return an arrayList of all the available toppings.
         * Don't forget to order the data coming from the database appropriately.
         *
         */
        connect_to_db();
        ArrayList<Topping> t = new ArrayList<Topping>();
        Statement stmt = conn.createStatement();

        try {
            PreparedStatement os;
            ResultSet rset;
            String query2;
            query2 = "Select * from topping order by topping_TopName asc;";
            os = conn.prepareStatement(query2);;
            rset = os.executeQuery();
            while(rset.next())
            {
                t.add(new Topping(
                        rset.getInt("topping_TopID"),
                        rset.getString("topping_TopName"),
                        rset.getDouble("topping_SmallAMT"),
                        rset.getDouble("topping_MedAMT"),
                        rset.getDouble("topping_LgAMT"),
                        rset.getDouble("topping_XLAMT"),
                        rset.getDouble("topping_CustPrice"),
                        rset.getDouble("topping_BusPrice"),
                        rset.getInt("topping_MinINVT"),
                        rset.getInt("topping_CurINVT")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return t;
    }

    public static Topping findToppingByName(String name) throws SQLException, IOException
    {
        /*
         * Query the database for the topping using it's name.
         * If found, then return a Topping object for the topping.
         * If it's not found....then return null
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();

        Topping t = null;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from topping where topping_TopName=?;";
            os = conn.prepareStatement(query1);
            os.setString(1, name);
            rset = os.executeQuery();
            while (rset.next()) {
                t = new Topping(
                        rset.getInt("topping_TopID"),
                        rset.getString("topping_TopName"),
                        rset.getDouble("topping_SmallAMT"),
                        rset.getDouble("topping_MedAMT"),
                        rset.getDouble("topping_LgAMT"),
                        rset.getDouble("topping_XLAMT"),
                        rset.getDouble("topping_CustPrice"),
                        rset.getDouble("topping_BusPrice"),
                        rset.getInt("topping_MinINVT"),
                        rset.getInt("topping_CurINVT")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return t;
    }

    public static ArrayList<Topping> getToppingsOnPizza(Pizza p) throws SQLException, IOException
    {
        connect_to_db();
        ArrayList<Topping> t = new ArrayList<Topping>();
        Statement stmt = conn.createStatement();

        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "Select * from pizza_topping p join topping t on p.topping_TopID = t.topping_TopID where pizza_PizzaID=?;";
            os = conn.prepareStatement(query);
            os.setInt(1, p.getPizzaID());
            rset = os.executeQuery();
            while(rset.next())
            {
                Topping topping = new Topping(
                        rset.getInt("topping_TopID"),
                        rset.getString("topping_TopName"),
                        rset.getDouble("topping_SmallAMT"),
                        rset.getDouble("topping_MedAMT"),
                        rset.getDouble("topping_LgAMT"),
                        rset.getDouble("topping_XLAMT"),
                        rset.getDouble("topping_CustPrice"),
                        rset.getDouble("topping_BusPrice"),
                        rset.getInt("topping_MinINVT"),
                        rset.getInt("topping_CurINVT")
                );
                if (rset.getInt("pizza_topping_IsDouble") == 2) {
                    topping.setDoubled(true);
                }
                t.add(topping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return t;
    }

    public static void addToInventory(int toppingID, double quantity) throws SQLException, IOException
    {
        /*
         * Updates the quantity of the topping in the database by the amount specified.
         *
         * */
        connect_to_db();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            int rounded = (int) Math.round(quantity);
            if (rounded > quantity && rounded < 0) {
                rounded -= 1;
            }
            query = "UPDATE topping SET topping_CurINVT = topping_CurINVT + ? WHERE topping_TopID = ?";
            os = conn.prepareStatement(query);
            os.setLong(1, rounded);
            os.setInt(2, toppingID);
            os.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }


        return;
    }


    public static ArrayList<Pizza> getPizzas(Order o) throws SQLException, IOException
    {
        /*
         * Build an ArrayList of all the Pizzas associated with the Order.
         *
         */
        connect_to_db();
        ArrayList<Pizza> p = new ArrayList<Pizza>();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query;
            query = "SELECT *\n" +
                    "FROM pizza\n" +
                    "WHERE ordertable_OrderID = ?";
            os = conn.prepareStatement(query);
            os.setInt(1, o.getOrderID());
            rset = os.executeQuery();
            Pizza i = null;
            while (rset.next()) {
                i = new Pizza(
                        rset.getInt("pizza_PizzaID"),
                        rset.getString("pizza_Size"),
                        rset.getString("pizza_CrustType"),
                        rset.getInt("ordertable_OrderID"),
                        rset.getString("pizza_PizzaState"),
                        rset.getString("pizza_PizzaDate"),
                        rset.getDouble("pizza_CustPrice"),
                        rset.getDouble("pizza_BusPrice")
                );
                i.setDiscounts(getDiscounts(i));
                i.setToppings(getToppingsOnPizza(i));
                p.add(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return p;
    }

    public static ArrayList<Discount> getDiscounts(Order o) throws SQLException, IOException
    {
        /*
         * Build an array list of all the Discounts associted with the Order.
         *
         */
        connect_to_db();
        ArrayList<Discount> d = new ArrayList<Discount>();
        Statement stmt = conn.createStatement();

        try {
            PreparedStatement os;
            ResultSet rset;
            String query2;
            query2 = "Select * from order_discount o join discount d on o.discount_DiscountID = d.discount_DiscountID " +
                    "where ordertable_OrderID = ? " +
                    "order by discount_DiscountName;";
            os = conn.prepareStatement(query2);;
            os.setInt(1, o.getOrderID());
            rset = os.executeQuery();
            while(rset.next())
            {
                d.add(new Discount(rset.getInt("discount_DiscountID"),
                        rset.getString("discount_DiscountName"),
                        rset.getDouble("discount_Amount"),
                        rset.getBoolean("discount_IsPercent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return d;
    }

    public static ArrayList<Discount> getDiscounts(Pizza p) throws SQLException, IOException
    {
        /*
         * Build an array list of all the Discounts associted with the Pizza.
         *
         */
        connect_to_db();
        ArrayList<Discount> d = new ArrayList<Discount>();
        Statement stmt = conn.createStatement();

        try {
            PreparedStatement os;
            ResultSet rset;
            String query2;
            query2 = "Select * from pizza_discount p " +
                    "join discount d on p.discount_DiscountID = d.discount_DiscountID " +
                    "where pizza_PizzaID = ?;";
            os = conn.prepareStatement(query2);;
            os.setDouble(1, p.getPizzaID());
            rset = os.executeQuery();
            while(rset.next())
            {
                d.add(new Discount(rset.getInt("discount_DiscountID"),
                        rset.getString("discount_DiscountName"),
                        rset.getDouble("discount_Amount"),
                        rset.getBoolean("discount_IsPercent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return d;
    }

    public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException
    {
        /*
         * Query the database fro the base customer price for that size and crust pizza.
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();

        double c = 0.00;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select baseprice_CustPrice from baseprice where baseprice_Size=? and baseprice_CrustType=?";
            os = conn.prepareStatement(query1);
            os.setString(1, size);
            os.setString(2, crust);
            rset = os.executeQuery();
            while (rset.next()) {
                c = rset.getDouble("baseprice_CustPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return c;
    }

    public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException
    {
        /*
         * Query the database fro the base business price for that size and crust pizza.
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();

        double c = 0.00;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select baseprice_BusPrice from baseprice where baseprice_Size=? and baseprice_CrustType=?";
            os = conn.prepareStatement(query1);
            os.setString(1, size);
            os.setString(2, crust);
            rset = os.executeQuery();
            while (rset.next()) {
                c = rset.getDouble("baseprice_BusPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }
        return c;
    }


    public static void printToppingReport() throws SQLException, IOException
    {
        /*
         * Prints the ToppingPopularity view. Remember that this view
         * needs to exist in your DB, so be sure you've run your createViews.sql
         * files on your testing DB if you haven't already.
         *
         * The result should be readable and sorted as indicated in the prompt.
         *
         * HINT: You need to match the expected output EXACTLY....I would suggest
         * you look at the printf method (rather that the simple print of println).
         * It operates the same in Java as it does in C and will make your code
         * better.
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from ToppingPopularity";
            os = conn.prepareStatement(query1);
            rset = os.executeQuery();
            System.out.printf("%-20s %-15s%n", "Topping", "Topping Count");
            System.out.printf("%-20s %-15s%n", "-------", "-------------");

            while (rset.next()) {
                System.out.printf("%-20s %-15s%n",
                        rset.getString("Topping"),
                        rset.getString("ToppingCount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        return;
    }

    public static void printProfitByPizzaReport() throws SQLException, IOException
    {
        /*
         * Prints the ProfitByPizza view. Remember that this view
         * needs to exist in your DB, so be sure you've run your createViews.sql
         * files on your testing DB if you haven't already.
         *
         * The result should be readable and sorted as indicated in the prompt.
         *
         * HINT: You need to match the expected output EXACTLY....I would suggest
         * you look at the printf method (rather that the simple print of println).
         * It operates the same in Java as it does in C and will make your code
         * better.
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from ProfitByPizza";
            os = conn.prepareStatement(query1);
            rset = os.executeQuery();
            System.out.printf("%-20s %-15s %-15s %-15s%n",
                    "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
            System.out.printf("%-20s %-15s %-15s %-15s%n",
                    "----------", "-----------", "------", "---------------");

            while (rset.next()) {
                System.out.printf("%-20s %-15s %-15.2f %-15s%n",
                        rset.getString("Size"),
                        rset.getString("Crust"),
                        rset.getDouble("Profit"),
                        rset.getString("OrderMonth")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

    }

    public static void printProfitByOrderTypeReport() throws SQLException, IOException
    {
        /*
         * Prints the ProfitByOrderType view. Remember that this view
         * needs to exist in your DB, so be sure you've run your createViews.sql
         * files on your testing DB if you haven't already.
         *
         * The result should be readable and sorted as indicated in the prompt.
         *
         * HINT: You need to match the expected output EXACTLY....I would suggest
         * you look at the printf method (rather that the simple print of println).
         * It operates the same in Java as it does in C and will make your code
         * better.
         *
         */
        connect_to_db();
        Statement stmt = conn.createStatement();

        double c = 0.00;
        try {
            PreparedStatement os;
            ResultSet rset;
            String query1;
            query1 = "Select * from ProfitByOrderType";
            os = conn.prepareStatement(query1);
            rset = os.executeQuery();
            System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "Customer Type","Order Month","Total Order Price","Total Order Cost","Profit");
            System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "-------------","-----------","-----------------","----------------","------");

            while (rset.next()) {
                System.out.printf("%-20s%-20s%-20.2f%-20.2f%-20.2f\n",
                        rset.getString("CustomerType"),
                        rset.getString("OrderMonth"),
                        rset.getDouble("TotalOrderPrice"),
                        rset.getDouble("TotalOrderCost"),
                        rset.getDouble("Profit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

    }



    /*
     * These private methods help get the individual components of an SQL datetime object.
     * You're welcome to keep them or remove them....but they are usefull!
     */
    private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(0,4));
    }
    private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(5, 7));
    }
    private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(8, 10));
    }

    public static boolean checkDate(int year, int month, int day, String dateOfOrder)
    {
        if(getYear(dateOfOrder) > year)
            return true;
        else if(getYear(dateOfOrder) < year)
            return false;
        else
        {
            if(getMonth(dateOfOrder) > month)
                return true;
            else if(getMonth(dateOfOrder) < month)
                return false;
            else
            {
                if(getDay(dateOfOrder) >= day)
                    return true;
                else
                    return false;
            }
        }
    }


}