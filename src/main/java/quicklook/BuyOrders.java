package quicklook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "buy_orders")
public class BuyOrders {

    List<Order> listOrders;

    @XmlElement(name = "order")
    public List<Order> getListOrders() {
        return listOrders;
    }

    public void setListOrders(List<Order> listOrders) {
        this.listOrders = listOrders;
    }

    public String toString() {
        String toString = "";
        for (Order o : listOrders) {
            toString = toString + o.toString() + "\n";
        }
        return toString;
    }
}
