package cpsc4620;

import java.util.ArrayList;

public class TestConnection {
    public static void main(String[] args) {
        try {
//            java.util.Date d =  new java.util.Date();
//            Discount dis = new Discount(2, "Gameday Special", 20.00, true);
//            Topping top = new Topping(1, "Pepperoni", 2.00, 2.75, 3.50, 4.50, 1.25, .20, 50, 100);
//            Topping top1 = new Topping(2, "Sausage", 2.50, 3.00, 3.50, 4.25, 1.25, .15, 50, 100);
//            top1.setDoubled(true);
//            Pizza p = new Pizza(17, "Large", "Original", 7, "completed", "2025-01-05 12:03:00", 18.00, 3.30);
//            p.addDiscounts(dis);
//            p.addToppings(top, top.getDoubled());
//            p.addToppings(top1, top1.getDoubled());
//            Order o = new Order(4, 1, "delivery", "789767878",1.00, .50, false );

//            ArrayList<Discount> d = DBNinja.getDiscounts(o);
            DBNinja.printProfitByPizzaReport();;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

