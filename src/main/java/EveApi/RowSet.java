package EveApi;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rowset")
public class RowSet {
    private List<CharOrder> listOrders;

    public List<CharOrder> getListOrders() {
        return listOrders;
    }

    @XmlElement(name = "row")
    public void setListOrders(List<CharOrder> listOrders) {
        this.listOrders = listOrders;
    }

    @Override
    public String toString() {
        String toString = "";
        for (CharOrder order : listOrders) {
            toString += order.toString() + "\n";
        }
        return toString;
    }
}