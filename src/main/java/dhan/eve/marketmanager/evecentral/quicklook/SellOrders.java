package dhan.eve.marketmanager.evecentral.quicklook;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "sell_orders")
public class SellOrders {

    List<Order> listOrders;

    @XmlElement(name = "order")
    public List<Order> getListOrders() {
        return listOrders;
    }

    public void setListOrders(List<Order> listOrders) {
        this.listOrders = listOrders;
    }

    public void printCompactPrices(){
        
        for(Order o: listOrders){
            System.out.println("Order: "+ o.getPrice()+ ", Volume remaining: "+ o.getVolumeRemaining());
        }
    }

    public String toString() {
        String toString = "";
        for (Order o : listOrders) {
            toString = toString + o.toString() + "\n";
        }
        return toString;
    }
}
